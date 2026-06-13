package com.dicoding.tugas_akhir.data.remote.request

data class CreatePaymentRequest(
    val bookingId: String,
    val paymentMethodId: String,
)