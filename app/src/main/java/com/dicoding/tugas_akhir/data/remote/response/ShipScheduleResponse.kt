package com.dicoding.tugas_akhir.data.remote.response

data class ShipScheduleResponse(
    val id: String,
    val shipName: String,
    val shipCode: String,
    val origin: String,
    val destination: String,
    val departureDate: String,
    val departureTime: String,
    val arrivalDate: String,
    val arrivalTime: String,
    val duration: String,
    val economyPrice: Int,
    val businessPrice: Int,
    val firstClassPrice: Int,
    val quota: Int,
    val status: String,
    val facilities: List<String>,
    val description: String,
    val canRefund: Boolean,
    val canReschedule: Boolean,
)