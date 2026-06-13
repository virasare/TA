package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.core.common.Resource
import com.dicoding.tugas_akhir.data.local.LocalDataSource
import com.dicoding.tugas_akhir.data.mapper.DataMapper
import com.dicoding.tugas_akhir.data.remote.datasource.FakeRemoteDataSource
import com.dicoding.tugas_akhir.data.remote.request.CreatePaymentRequest
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.domain.model.Payment
import com.dicoding.tugas_akhir.domain.model.PaymentMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PaymentRepository private constructor(
    private val remoteDataSource: FakeRemoteDataSource,
    private val localDataSource: LocalDataSource,
) {

    fun getPaymentMethods(): Flow<Resource<List<PaymentMethod>>> = flow {
        emit(Resource.Loading)

        val response = remoteDataSource.getPaymentMethods()

        if (response.isEmpty()) {
            emit(Resource.Empty)
        } else {
            emit(Resource.Success(DataMapper.mapPaymentMethodResponsesToDomain(response)))
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil metode pembayaran"))
    }

    fun createPayment(
        request: CreatePaymentRequest,
    ): Flow<Resource<Payment>> = flow {
        emit(Resource.Loading)

        val payment = try {
            val response = remoteDataSource.createPayment(request)
            DataMapper.mapPaymentResponseToDomain(response)
        } catch (exception: Exception) {
            val localBooking = localDataSource.getBookingById(request.bookingId)
                ?: throw IllegalArgumentException("Pesanan tidak ditemukan")

            val booking = DataMapper.mapBookingWithPassengersToDomain(localBooking)

            buildLocalPayment(
                request = request,
                booking = booking,
            )
        }

        localDataSource.savePayment(payment)

        emit(Resource.Success(payment))
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal membuat pembayaran"))
    }

    fun getPaymentDetail(
        paymentId: String,
    ): Flow<Resource<Payment>> = flow {
        emit(Resource.Loading)

        val localPayment = localDataSource.getPaymentById(paymentId)

        if (localPayment != null) {
            emit(Resource.Success(DataMapper.mapPaymentEntityToDomain(localPayment)))
            return@flow
        }

        val response = remoteDataSource.getPaymentDetail(paymentId)

        if (response == null) {
            emit(Resource.Error("Data pembayaran tidak ditemukan"))
        } else {
            val payment = DataMapper.mapPaymentResponseToDomain(response)
            localDataSource.savePayment(payment)
            emit(Resource.Success(payment))
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil detail pembayaran"))
    }

    fun simulatePaymentSuccess(
        paymentId: String,
    ): Flow<Resource<Payment>> = flow {
        emit(Resource.Loading)

        val payment = try {
            val response = remoteDataSource.simulatePaymentSuccess(paymentId)
            DataMapper.mapPaymentResponseToDomain(response)
        } catch (exception: Exception) {
            val localPayment = localDataSource.getPaymentById(paymentId)
                ?: throw IllegalArgumentException("Data pembayaran tidak ditemukan")

            DataMapper.mapPaymentEntityToDomain(localPayment).copy(
                status = "Berhasil",
            )
        }

        localDataSource.savePayment(payment)
        localDataSource.updateBookingStatus(
            bookingId = payment.bookingId,
            status = "Aktif",
        )

        emit(Resource.Success(payment))
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengecek status pembayaran"))
    }

    private fun buildLocalPayment(
        request: CreatePaymentRequest,
        booking: Booking,
    ): Payment {
        val method = getPaymentMethodById(request.paymentMethodId)

        return Payment(
            id = "PAY-${UUID.randomUUID().toString().take(8).uppercase()}",
            bookingId = request.bookingId,
            paymentMethodId = method.id,
            paymentMethodName = method.name,
            totalPrice = booking.totalPrice,
            paymentCode = generatePaymentCode(method.id),
            status = "Menunggu Pembayaran",
            expiredIn = "30 menit",
            instructions = generatePaymentInstructions(method.id),
            createdAt = getCurrentDateTime(),
        )
    }

    private fun getPaymentMethodById(
        methodId: String,
    ): PaymentMethod {
        return when (methodId) {
            "virtual_account" -> PaymentMethod(
                id = "virtual_account",
                name = "Virtual Account",
                description = "Bayar melalui ATM, mobile banking, atau internet banking.",
            )

            "qris" -> PaymentMethod(
                id = "qris",
                name = "QRIS",
                description = "Bayar dengan scan QR menggunakan e-wallet atau mobile banking.",
            )

            "bank_transfer" -> PaymentMethod(
                id = "bank_transfer",
                name = "Transfer Bank",
                description = "Transfer manual ke rekening tujuan yang tersedia.",
            )

            else -> PaymentMethod(
                id = methodId,
                name = "Metode Pembayaran",
                description = "Metode pembayaran yang dipilih.",
            )
        }
    }

    private fun generatePaymentCode(
        methodId: String,
    ): String {
        return when (methodId) {
            "virtual_account" -> "8808${System.currentTimeMillis().toString().takeLast(8)}"
            "qris" -> "QRIS-${UUID.randomUUID().toString().take(10).uppercase()}"
            "bank_transfer" -> "1234567890"
            else -> "-"
        }
    }

    private fun generatePaymentInstructions(
        methodId: String,
    ): List<String> {
        return when (methodId) {
            "virtual_account" -> listOf(
                "Buka aplikasi mobile banking atau ATM.",
                "Pilih menu Virtual Account.",
                "Masukkan nomor Virtual Account yang tersedia.",
                "Pastikan nominal pembayaran sudah sesuai.",
                "Konfirmasi pembayaran.",
            )

            "qris" -> listOf(
                "Buka aplikasi e-wallet atau mobile banking.",
                "Pilih menu Scan QRIS.",
                "Scan kode QR yang tersedia.",
                "Pastikan nominal pembayaran sudah sesuai.",
                "Konfirmasi pembayaran.",
            )

            "bank_transfer" -> listOf(
                "Buka aplikasi mobile banking atau ATM.",
                "Pilih menu Transfer Bank.",
                "Masukkan nomor rekening tujuan.",
                "Masukkan nominal pembayaran sesuai total pesanan.",
                "Simpan bukti pembayaran.",
            )

            else -> listOf(
                "Ikuti instruksi pembayaran yang tersedia.",
            )
        }
    }

    private fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
        return formatter.format(Date())
    }

    companion object {
        @Volatile
        private var INSTANCE: PaymentRepository? = null

        fun getInstance(
            remoteDataSource: FakeRemoteDataSource,
            localDataSource: LocalDataSource,
        ): PaymentRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = PaymentRepository(
                    remoteDataSource = remoteDataSource,
                    localDataSource = localDataSource,
                )
                INSTANCE = instance
                instance
            }
        }
    }
}