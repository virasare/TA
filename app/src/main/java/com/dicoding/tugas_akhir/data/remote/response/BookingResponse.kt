package com.dicoding.tugas_akhir.data.remote.response

data class BookingResponse(
    val id: String,
    val scheduleId: String,
    val shipName: String,
    val origin: String,
    val destination: String,
    val departureDate: String,
    val departureTime: String,
    val ticketClassName: String,
    val ticketPrice: Int,
    val passengerCount: Int,
    val passengers: List<PassengerResponse>,
    val adminFee: Int,
    val totalPrice: Int,
    val status: String,
    val createdAt: String,
    val paymentDeadline: String,
)