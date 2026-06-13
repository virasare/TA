package com.dicoding.tugas_akhir.domain.model

data class ETicket(
    val bookingId: String,
    val bookingCode: String,
    val paymentId: String?,
    val shipName: String,
    val origin: String,
    val destination: String,
    val departureDate: String,
    val departureTime: String,
    val ticketClassName: String,
    val passengers: List<Passenger>,
    val status: String,
    val qrCode: String,
    val issuedAt: String,
    val terminal: String,
    val gate: String,
    val note: String,
)