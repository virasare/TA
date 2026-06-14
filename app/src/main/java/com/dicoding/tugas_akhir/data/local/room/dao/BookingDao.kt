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
interface BookingDao {

    @Transaction
    @Query("SELECT * FROM bookings ORDER BY createdAtMillis DESC")
    fun getAllBookings(): Flow<List<BookingWithPassengers>>

    @Transaction
    @Query("SELECT * FROM bookings WHERE id = :bookingId LIMIT 1")
    suspend fun getBookingById(
        bookingId: String,
    ): BookingWithPassengers?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(
        booking: BookingEntity,
    ): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassengers(
        passengers: List<PassengerEntity>,
    ): List<Long>

    @Query("DELETE FROM passengers WHERE bookingId = :bookingId")
    suspend fun deletePassengersByBookingId(
        bookingId: String,
    ): Int

    @Query("UPDATE bookings SET status = :status WHERE id = :bookingId")
    suspend fun updateBookingStatus(
        bookingId: String,
        status: String,
    ): Int
}