package com.dicoding.tugas_akhir.domain.model

data class Payment(
    val id: String,
    val bookingId: String,
    val paymentMethodId: String,
    val paymentMethodName: String,
    val totalPrice: Int,
    val paymentCode: String,
    val status: String,
    val expiredIn: String,
    val instructions: List<String>,
    val createdAt: String,
)