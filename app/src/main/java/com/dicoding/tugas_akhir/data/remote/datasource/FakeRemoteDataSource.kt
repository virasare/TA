package com.dicoding.tugas_akhir.data.remote.datasource

import com.dicoding.tugas_akhir.data.dummy.DummyShipScheduleApiData
import com.dicoding.tugas_akhir.data.remote.request.CreateBookingRequest
import com.dicoding.tugas_akhir.data.remote.request.CreatePaymentRequest
import com.dicoding.tugas_akhir.data.remote.response.BookingResponse
import com.dicoding.tugas_akhir.data.remote.response.PassengerResponse
import com.dicoding.tugas_akhir.data.remote.response.PaymentMethodResponse
import com.dicoding.tugas_akhir.data.remote.response.PaymentResponse
import com.dicoding.tugas_akhir.data.remote.response.ShipScheduleResponse
import com.dicoding.tugas_akhir.domain.model.TicketClassOption
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class FakeRemoteDataSource private constructor() {

    private val bookings = mutableListOf<BookingResponse>()
    private val payments = mutableListOf<PaymentResponse>()

    suspend fun getUpcomingSchedules(): List<ShipScheduleResponse> {
        delay(600)

        return DummyShipScheduleApiData.schedules
    }

    suspend fun searchSchedules(
        origin: String,
        destination: String,
        date: String,
    ): List<ShipScheduleResponse> {
        delay(900)

        return DummyShipScheduleApiData.schedules.filter { schedule ->
            val isOriginMatch = schedule.origin.contains(origin, ignoreCase = true)
            val isDestinationMatch = schedule.destination.contains(destination, ignoreCase = true)
            val isDateMatch = schedule.departureDate == date

            isOriginMatch && isDestinationMatch && isDateMatch
        }
    }

    suspend fun getScheduleDetail(scheduleId: String): ShipScheduleResponse? {
        delay(600)

        return DummyShipScheduleApiData.schedules.find { schedule ->
            schedule.id == scheduleId
        }
    }

    suspend fun getTicketClassOptions(scheduleId: String): List<TicketClassOption> {
        delay(600)

        val schedule = getScheduleDetail(scheduleId) ?: return emptyList()

        return listOf(
            TicketClassOption(
                id = "economy",
                name = "Ekonomi",
                price = schedule.economyPrice,
                description = "Pilihan hemat untuk perjalanan antar pulau.",
            ),
            TicketClassOption(
                id = "business",
                name = "Bisnis",
                price = schedule.businessPrice,
                description = "Tempat duduk lebih nyaman dengan fasilitas tambahan.",
            ),
            TicketClassOption(
                id = "first_class",
                name = "Kelas I",
                price = schedule.firstClassPrice,
                description = "Pilihan terbaik dengan kenyamanan lebih maksimal.",
            ),
        )
    }

    suspend fun createBooking(request: CreateBookingRequest): BookingResponse {
        delay(1200)

        val schedule = getScheduleDetail(request.scheduleId)
            ?: throw IllegalArgumentException("Jadwal tidak ditemukan")

        val ticketClass = getTicketClassOptions(request.scheduleId)
            .find { it.id == request.ticketClassId }
            ?: throw IllegalArgumentException("Kelas tiket tidak ditemukan")

        val passengers = request.passengers.mapIndexed { index, passenger ->
            PassengerResponse(
                id = "PSG-${System.currentTimeMillis()}-$index",
                fullName = passenger.fullName,
                nik = passenger.nik,
                phoneNumber = passenger.phoneNumber,
                birthDate = passenger.birthDate,
                gender = passenger.gender,
            )
        }

        val adminFee = 5000
        val totalTicketPrice = ticketClass.price * passengers.size
        val totalPrice = totalTicketPrice + adminFee

        val booking = BookingResponse(
            id = "BKG-${UUID.randomUUID().toString().take(8).uppercase()}",
            scheduleId = schedule.id,
            shipName = schedule.shipName,
            origin = schedule.origin,
            destination = schedule.destination,
            departureDate = schedule.departureDate,
            departureTime = schedule.departureTime,
            ticketClassName = ticketClass.name,
            ticketPrice = ticketClass.price,
            passengerCount = passengers.size,
            passengers = passengers,
            adminFee = adminFee,
            totalPrice = totalPrice,
            status = "Menunggu Pembayaran",
            createdAt = getCurrentDateTime(),
            paymentDeadline = "30 menit",
        )

        bookings.add(0, booking)
        return booking
    }

    suspend fun getBookingDetail(bookingId: String): BookingResponse? {
        delay(600)

        return bookings.find { booking ->
            booking.id == bookingId
        }
    }

    suspend fun getPaymentMethods(): List<PaymentMethodResponse> {
        delay(500)

        return listOf(
            PaymentMethodResponse(
                id = "virtual_account",
                name = "Virtual Account",
                description = "Bayar melalui ATM, mobile banking, atau internet banking.",
            ),
            PaymentMethodResponse(
                id = "qris",
                name = "QRIS",
                description = "Bayar dengan scan QR menggunakan e-wallet atau mobile banking.",
            ),
            PaymentMethodResponse(
                id = "bank_transfer",
                name = "Transfer Bank",
                description = "Transfer manual ke rekening tujuan yang tersedia.",
            ),
        )
    }

    suspend fun createPayment(request: CreatePaymentRequest): PaymentResponse {
        delay(1000)

        val booking = getBookingDetail(request.bookingId)
            ?: throw IllegalArgumentException("Pesanan tidak ditemukan")

        val method = getPaymentMethods()
            .find { it.id == request.paymentMethodId }
            ?: throw IllegalArgumentException("Metode pembayaran tidak ditemukan")

        val payment = PaymentResponse(
            id = "PAY-${UUID.randomUUID().toString().take(8).uppercase()}",
            bookingId = booking.id,
            paymentMethodId = method.id,
            paymentMethodName = method.name,
            totalPrice = booking.totalPrice,
            paymentCode = generatePaymentCode(method.id),
            status = "Menunggu Pembayaran",
            expiredIn = "30 menit",
            instructions = generatePaymentInstructions(method.id),
            createdAt = getCurrentDateTime(),
        )

        payments.removeAll { it.bookingId == booking.id }
        payments.add(0, payment)

        return payment
    }

    suspend fun getPaymentDetail(paymentId: String): PaymentResponse? {
        delay(600)

        return payments.find { payment ->
            payment.id == paymentId
        }
    }

    suspend fun simulatePaymentSuccess(paymentId: String): PaymentResponse {
        delay(1000)

        val index = payments.indexOfFirst { payment ->
            payment.id == paymentId
        }

        if (index == -1) {
            throw IllegalArgumentException("Data pembayaran tidak ditemukan")
        }

        val currentPayment = payments[index]
        val updatedPayment = currentPayment.copy(
            status = "Berhasil",
        )

        payments[index] = updatedPayment
        updateBookingStatus(
            bookingId = updatedPayment.bookingId,
            status = "Aktif",
        )

        return updatedPayment
    }

    private fun updateBookingStatus(
        bookingId: String,
        status: String,
    ) {
        val index = bookings.indexOfFirst { booking ->
            booking.id == bookingId
        }

        if (index != -1) {
            bookings[index] = bookings[index].copy(
                status = status,
            )
        }
    }

    private fun generatePaymentCode(methodId: String): String {
        return when (methodId) {
            "virtual_account" -> "8808${System.currentTimeMillis().toString().takeLast(8)}"
            "qris" -> "QRIS-${UUID.randomUUID().toString().take(10).uppercase()}"
            "bank_transfer" -> "1234567890"
            else -> "-"
        }
    }

    private fun generatePaymentInstructions(methodId: String): List<String> {
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
        private var INSTANCE: FakeRemoteDataSource? = null

        fun getInstance(): FakeRemoteDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = FakeRemoteDataSource()
                INSTANCE = instance
                instance
            }
        }
    }
}