package com.dicoding.tugas_akhir.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dicoding.tugas_akhir.data.local.room.entity.BookingEntity
import com.dicoding.tugas_akhir.data.local.room.entity.BookingWithPassengers
import com.dicoding.tugas_akhir.data.local.room.entity.PassengerEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BookingDao {

    @Transaction
    @Query("SELECT * FROM bookings ORDER BY createdAtMillis DESC")
    abstract fun getAllBookings(): Flow<List<BookingWithPassengers>>

    @Transaction
    @Query("SELECT * FROM bookings WHERE id = :bookingId LIMIT 1")
    abstract suspend fun getBookingById(
        bookingId: String,
    ): BookingWithPassengers?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBooking(
        booking: BookingEntity,
    ): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPassengers(
        passengers: List<PassengerEntity>,
    ): List<Long>

    @Query("DELETE FROM passengers WHERE bookingId = :bookingId")
    abstract suspend fun deletePassengersByBookingId(
        bookingId: String,
    ): Int

    @Query("UPDATE bookings SET status = :status WHERE id = :bookingId")
    abstract suspend fun updateBookingStatus(
        bookingId: String,
        status: String,
    ): Int

    @Transaction
    open suspend fun insertBookingWithPassengers(
        booking: BookingEntity,
        passengers: List<PassengerEntity>,
    ): Boolean {
        insertBooking(booking)
        deletePassengersByBookingId(booking.id)
        insertPassengers(passengers)

        return true
    }
}