package com.dicoding.tugas_akhir.data.dummy

import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus

data class ShipSchedule(
    val id: Int,
    val shipName: String,
    val route: String,
    val departureDate: String,
    val departureTime: String,
    val arrivalDate: String,
    val arrivalTime: String,
    val duration: String,
    val price: String,
    val quota: String,
    val status: ShipScheduleStatus
)

val dummyShipSchedules = listOf(
    ShipSchedule(
        id = 1,
        shipName = "KM Nusa Bahari",
        route = "Ende → Surabaya",
        departureDate = "12 Jul 2026",
        departureTime = "18.00 WITA",
        arrivalDate = "14 Jul 2026",
        arrivalTime = "08.00 WIB",
        duration = "±38 jam",
        price = "Rp350.000",
        quota = "24 kursi",
        status = ShipScheduleStatus.Available
    ),
    ShipSchedule(
        id = 2,
        shipName = "KM Laut Flores",
        route = "Ende → Denpasar",
        departureDate = "13 Jul 2026",
        departureTime = "20.30 WITA",
        arrivalDate = "15 Jul 2026",
        arrivalTime = "09.00 WITA",
        duration = "±36 jam",
        price = "Rp420.000",
        quota = "8 kursi",
        status = ShipScheduleStatus.Limited
    ),
    ShipSchedule(
        id = 3,
        shipName = "KM Samudra Timur",
        route = "Kupang → Surabaya",
        departureDate = "14 Jul 2026",
        departureTime = "16.00 WITA",
        arrivalDate = "16 Jul 2026",
        arrivalTime = "06.00 WIB",
        duration = "±40 jam",
        price = "Rp500.000",
        quota = "0 kursi",
        status = ShipScheduleStatus.Unavailable
    ),
    ShipSchedule(
        id = 4,
        shipName = "KM Flores Jaya",
        route = "Labuan Bajo → Denpasar",
        departureDate = "15 Jul 2026",
        departureTime = "19.00 WITA",
        arrivalDate = "16 Jul 2026",
        arrivalTime = "23.00 WITA",
        duration = "±28 jam",
        price = "Rp375.000",
        quota = "18 kursi",
        status = ShipScheduleStatus.Available
    ),
    ShipSchedule(
        id = 5,
        shipName = "KM Nusantara Raya",
        route = "Maumere → Makassar",
        departureDate = "16 Jul 2026",
        departureTime = "15.30 WITA",
        arrivalDate = "18 Jul 2026",
        arrivalTime = "07.00 WITA",
        duration = "±39 jam",
        price = "Rp390.000",
        quota = "6 kursi",
        status = ShipScheduleStatus.Limited
    ),
    ShipSchedule(
        id = 6,
        shipName = "KM Bahari Sentosa",
        route = "Ende → Makassar",
        departureDate = "17 Jul 2026",
        departureTime = "21.00 WITA",
        arrivalDate = "19 Jul 2026",
        arrivalTime = "10.00 WITA",
        duration = "±37 jam",
        price = "Rp410.000",
        quota = "32 kursi",
        status = ShipScheduleStatus.Available
    ),
    ShipSchedule(
        id = 7,
        shipName = "KM Lintas Pulau",
        route = "Kupang → Denpasar",
        departureDate = "18 Jul 2026",
        departureTime = "17.00 WITA",
        arrivalDate = "20 Jul 2026",
        arrivalTime = "05.30 WITA",
        duration = "±36 jam",
        price = "Rp450.000",
        quota = "0 kursi",
        status = ShipScheduleStatus.Unavailable
    )
)