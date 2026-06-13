package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.core.common.Resource
import com.dicoding.tugas_akhir.data.local.LocalDataSource
import com.dicoding.tugas_akhir.data.mapper.DataMapper
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.domain.model.ETicket
import com.dicoding.tugas_akhir.domain.model.Payment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyTicketRepository private constructor(
    private val localDataSource: LocalDataSource,
) {

    fun getMyTickets(): Flow<Resource<List<Booking>>> = flow {
        emit(Resource.Loading)

        localDataSource.getAllBookings().collect { response ->
            val tickets = response.map { bookingWithPassengers ->
                DataMapper.mapBookingWithPassengersToDomain(bookingWithPassengers)
            }

            if (tickets.isEmpty()) {
                emit(Resource.Empty)
            } else {
                emit(Resource.Success(tickets))
            }
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil daftar tiket"))
    }

    fun getETicketByBookingId(
        bookingId: String,
    ): Flow<Resource<ETicket>> = flow {
        emit(Resource.Loading)

        val bookingWithPassengers = localDataSource.getBookingById(bookingId)

        if (bookingWithPassengers == null) {
            emit(Resource.Error("E-ticket tidak ditemukan"))
            return@flow
        }

        val booking = DataMapper.mapBookingWithPassengersToDomain(bookingWithPassengers)
        val payment = localDataSource.getPaymentByBookingId(bookingId)
            ?.let { DataMapper.mapPaymentEntityToDomain(it) }

        emit(
            Resource.Success(
                buildETicket(
                    booking = booking,
                    payment = payment,
                )
            )
        )
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil e-ticket"))
    }

    fun getETicketByPaymentId(
        paymentId: String,
    ): Flow<Resource<ETicket>> = flow {
        emit(Resource.Loading)

        val paymentEntity = localDataSource.getPaymentById(paymentId)

        if (paymentEntity == null) {
            emit(Resource.Error("E-ticket tidak ditemukan"))
            return@flow
        }

        val payment = DataMapper.mapPaymentEntityToDomain(paymentEntity)
        val bookingWithPassengers = localDataSource.getBookingById(payment.bookingId)

        if (bookingWithPassengers == null) {
            emit(Resource.Error("Pesanan tidak ditemukan"))
            return@flow
        }

        val booking = DataMapper.mapBookingWithPassengersToDomain(bookingWithPassengers)

        emit(
            Resource.Success(
                buildETicket(
                    booking = booking,
                    payment = payment,
                )
            )
        )
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil e-ticket"))
    }

    private fun buildETicket(
        booking: Booking,
        payment: Payment?,
    ): ETicket {
        return ETicket(
            bookingId = booking.id,
            bookingCode = booking.id.replace("BKG", "NKP"),
            paymentId = payment?.id,
            shipName = booking.shipName,
            origin = booking.origin,
            destination = booking.destination,
            departureDate = booking.departureDate,
            departureTime = booking.departureTime,
            ticketClassName = booking.ticketClassName,
            passengers = booking.passengers,
            status = booking.status,
            qrCode = "ETICKET-${booking.id}-${payment?.id ?: "UNPAID"}",
            issuedAt = getCurrentDateTime(),
            terminal = "Pelabuhan ${booking.origin}",
            gate = "Gate 2",
            note = "Tunjukkan e-ticket ini kepada petugas pelabuhan saat proses check-in.",
        )
    }

    private fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
        return formatter.format(Date())
    }

    companion object {
        @Volatile
        private var INSTANCE: MyTicketRepository? = null

        fun getInstance(
            localDataSource: LocalDataSource,
        ): MyTicketRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = MyTicketRepository(
                    localDataSource = localDataSource,
                )
                INSTANCE = instance
                instance
            }
        }
    }
}