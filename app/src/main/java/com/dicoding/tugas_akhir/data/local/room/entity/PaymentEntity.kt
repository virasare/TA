package com.dicoding.tugas_akhir.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey
    val id: String,
    val bookingId: String,
    val paymentMethodId: String,
    val paymentMethodName: String,
    val totalPrice: Int,
    val paymentCode: String,
    val status: String,
    val expiredIn: String,
    val instructions: String,
    val createdAt: String,
    val createdAtMillis: Long,
)