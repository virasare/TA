package com.dicoding.tugas_akhir.data.remote.response

data class PaymentResponse(
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