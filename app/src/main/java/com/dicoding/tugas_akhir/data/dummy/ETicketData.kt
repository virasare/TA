package com.dicoding.tugas_akhir.data.dummy

data class ETicketData(
    val bookingCode: String,
    val shipName: String,
    val route: String,
    val ticketClass: String,
    val passengerCount: Int,
    val passengers: List<PassengerData>,
    val departureDate: String,
    val departureTime: String,
    val arrivalDate: String,
    val arrivalTime: String,
    val duration: String,
    val originPort: String,
    val destinationPort: String,
    val status: String
)

fun createETicketFromCurrentBooking(
    schedule: ShipSchedule?,
    selectedTicket: TicketClassOption?,
    passengerList: List<PassengerData>,
    bookingCode: String = "NKP12345"
): ETicketData {
    val route = schedule?.route ?: "Ende → Surabaya"
    val originCity = route.substringBefore("→").trim()
    val destinationCity = route.substringAfter("→").trim()

    return ETicketData(
        bookingCode = bookingCode,
        shipName = schedule?.shipName ?: "KM Dharma Rucitra VIII",
        route = route,
        ticketClass = selectedTicket?.name ?: "Ekonomi",
        passengerCount = selectedTicket?.passengerCount ?: passengerList.size.coerceAtLeast(1),
        passengers = passengerList.ifEmpty {
            listOf(
                PassengerData(
                    fullName = "Vira Sare",
                    nik = "5308123456789000",
                    phoneNumber = "081234567890",
                    gender = "Perempuan"
                )
            )
        },
        departureDate = schedule?.departureDate ?: "12 Jul 2026",
        departureTime = schedule?.departureTime ?: "18.00 WITA",
        arrivalDate = schedule?.arrivalDate ?: "14 Jul 2026",
        arrivalTime = schedule?.arrivalTime ?: "08.00 WIB",
        duration = schedule?.duration ?: "±38 jam",
        originPort = cityToPortNameForTicket(originCity),
        destinationPort = cityToPortNameForTicket(destinationCity),
        status = "Aktif"
    )
}

fun BookingOrder.toETicketData(): ETicketData {
    val originCity = route.substringBefore("→").trim()
    val destinationCity = route.substringAfter("→").trim()

    val passengers = List(passengerCount) { index ->
        PassengerData(
            fullName = if (index == 0) "Vira Sare" else "Penumpang ${index + 1}",
            nik = if (index == 0) "5308123456789000" else "5308xxxxxxxxxxxx",
            phoneNumber = if (index == 0) "081234567890" else "0812xxxxxxxx",
            gender = if (index == 0) "Perempuan" else "-"
        )
    }

    return ETicketData(
        bookingCode = bookingCode,
        shipName = shipName,
        route = route,
        ticketClass = ticketClass,
        passengerCount = passengerCount,
        passengers = passengers,
        departureDate = departureDate,
        departureTime = departureTime,
        arrivalDate = "-",
        arrivalTime = "-",
        duration = "-",
        originPort = cityToPortNameForTicket(originCity),
        destinationPort = cityToPortNameForTicket(destinationCity),
        status = status.label
    )
}

private fun cityToPortNameForTicket(city: String): String {
    return when (city.lowercase()) {
        "ende" -> "Pelabuhan IPPI, Ende"
        "surabaya" -> "Pelabuhan Tanjung Perak, Surabaya"
        "denpasar", "bali" -> "Pelabuhan Benoa, Denpasar"
        "kupang" -> "Pelabuhan Tenau Kupang"
        "labuan bajo" -> "Pelabuhan Labuan Bajo"
        "maumere" -> "Pelabuhan Laurens Say"
        "makassar" -> "Pelabuhan Makassar"
        else -> "Pelabuhan $city"
    }
}