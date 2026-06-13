package com.dicoding.tugas_akhir.data.remote.request

data class CreateBookingRequest(
    val scheduleId: String,
    val ticketClassId: String,
    val passengers: List<PassengerRequest>,
)