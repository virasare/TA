package com.dicoding.tugas_akhir.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.tugas_akhir.data.local.room.entity.PaymentEntity

@Dao
interface PaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(
        payment: PaymentEntity,
    ): Long

    @Query("SELECT * FROM payments WHERE id = :paymentId LIMIT 1")
    suspend fun getPaymentById(
        paymentId: String,
    ): PaymentEntity?

    @Query("SELECT * FROM payments WHERE bookingId = :bookingId ORDER BY createdAtMillis DESC LIMIT 1")
    suspend fun getPaymentByBookingId(
        bookingId: String,
    ): PaymentEntity?

    @Query("UPDATE payments SET status = :status WHERE id = :paymentId")
    suspend fun updatePaymentStatus(
        paymentId: String,
        status: String,
    ): Int
}