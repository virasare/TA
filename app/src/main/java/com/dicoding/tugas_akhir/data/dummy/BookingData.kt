package com.dicoding.tugas_akhir.data.dummy

enum class BookingStatus(
    val label: String
) {
    Active("Aktif"),
    WaitingPayment("Menunggu Bayar"),
    Completed("Selesai"),
    Cancelled("Dibatalkan"),
    RefundProcess("Refund Diproses"),
    Rescheduled("Reschedule")
}

data class TicketClassOption(
    val name: String,
    val price: Int,
    val passengerCount: Int = 0
)

data class PassengerData(
    val fullName: String = "",
    val nik: String = "",
    val phoneNumber: String = "",
    val gender: String = ""
)

data class BookingOrder(
    val id: Int,
    val bookingCode: String,
    val shipName: String,
    val route: String,
    val departureDate: String,
    val departureTime: String,
    val ticketClass: String,
    val passengerCount: Int,
    val totalPrice: Int,
    val status: BookingStatus
)

fun Int.toRupiah(): String {
    return "Rp%,d".format(this).replace(",", ".")
}

fun String.toPriceNumber(): Int {
    return replace("Rp", "")
        .replace(".", "")
        .replace(",", "")
        .trim()
        .toIntOrNull() ?: 0
}

val dummyOrders = listOf(
    BookingOrder(
        id = 1,
        bookingCode = "BK240612001",
        shipName = "KM Dharma Rucitra VIII",
        route = "Ende → Surabaya",
        departureDate = "12 Jun 2026",
        departureTime = "08.00",
        ticketClass = "Ekonomi",
        passengerCount = 1,
        totalPrice = 355000,
        status = BookingStatus.Active
    ),
    BookingOrder(
        id = 2,
        bookingCode = "BK240612002",
        shipName = "KM Dharma Rucitra VIII",
        route = "Ende → Surabaya",
        departureDate = "12 Jun 2026",
        departureTime = "08.00",
        ticketClass = "Bisnis",
        passengerCount = 2,
        totalPrice = 1005000,
        status = BookingStatus.WaitingPayment
    ),
    BookingOrder(
        id = 3,
        bookingCode = "BK240612003",
        shipName = "KM Nusa Bahari",
        route = "Ende → Denpasar",
        departureDate = "15 Jun 2026",
        departureTime = "18.00",
        ticketClass = "Ekonomi",
        passengerCount = 1,
        totalPrice = 425000,
        status = BookingStatus.Completed
    ),
    BookingOrder(
        id = 4,
        bookingCode = "BK240612004",
        shipName = "KM Laut Flores",
        route = "Kupang → Surabaya",
        departureDate = "18 Jun 2026",
        departureTime = "20.00",
        ticketClass = "Ekonomi",
        passengerCount = 1,
        totalPrice = 505000,
        status = BookingStatus.Cancelled
    )
)