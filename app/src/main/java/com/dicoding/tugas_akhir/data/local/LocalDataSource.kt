package com.dicoding.tugas_akhir.data.local

import com.dicoding.tugas_akhir.data.local.room.dao.BookingDao
import com.dicoding.tugas_akhir.data.local.room.dao.PaymentDao
import com.dicoding.tugas_akhir.data.local.room.entity.BookingWithPassengers
import com.dicoding.tugas_akhir.data.local.room.entity.PaymentEntity
import com.dicoding.tugas_akhir.data.mapper.DataMapper
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.domain.model.Payment
import kotlinx.coroutines.flow.Flow

class LocalDataSource private constructor(
    private val bookingDao: BookingDao,
    private val paymentDao: PaymentDao,
) {

    fun getAllBookings(): Flow<List<BookingWithPassengers>> {
        return bookingDao.getAllBookings()
    }

    suspend fun getBookingById(
        bookingId: String,
    ): BookingWithPassengers? {
        return bookingDao.getBookingById(bookingId)
    }

    suspend fun saveBooking(
        booking: Booking,
    ) {
        val bookingEntity = DataMapper.mapBookingDomainToEntity(booking)

        val passengerEntities = booking.passengers.mapIndexed { index, passenger ->
            DataMapper.mapPassengerDomainToEntity(
                input = passenger,
                bookingId = booking.id,
                passengerOrder = index,
            )
        }

        bookingDao.insertBookingWithPassengers(
            booking = bookingEntity,
            passengers = passengerEntities,
        )
    }

    suspend fun updateBookingStatus(
        bookingId: String,
        status: String,
    ) {
        bookingDao.updateBookingStatus(
            bookingId = bookingId,
            status = status,
        )
    }

    suspend fun savePayment(
        payment: Payment,
    ) {
        paymentDao.insertPayment(
            payment = DataMapper.mapPaymentDomainToEntity(payment),
        )
    }

    suspend fun getPaymentById(
        paymentId: String,
    ): PaymentEntity? {
        return paymentDao.getPaymentById(paymentId)
    }

    suspend fun getPaymentByBookingId(
        bookingId: String,
    ): PaymentEntity? {
        return paymentDao.getPaymentByBookingId(bookingId)
    }

    suspend fun updatePaymentStatus(
        paymentId: String,
        status: String,
    ) {
        paymentDao.updatePaymentStatus(
            paymentId = paymentId,
            status = status,
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(
            bookingDao: BookingDao,
            paymentDao: PaymentDao,
        ): LocalDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = LocalDataSource(
                    bookingDao = bookingDao,
                    paymentDao = paymentDao,
                )
                INSTANCE = instance
                instance
            }
        }
    }
}