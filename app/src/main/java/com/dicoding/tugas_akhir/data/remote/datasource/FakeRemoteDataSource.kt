package com.dicoding.tugas_akhir.data.remote.datasource

import com.dicoding.tugas_akhir.data.dummy.DummyShipScheduleApiData
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.dummy.dummyShipSchedules
import com.dicoding.tugas_akhir.data.remote.request.CreateBookingRequest
import com.dicoding.tugas_akhir.data.remote.response.BookingResponse
import com.dicoding.tugas_akhir.data.remote.response.PassengerResponse
import com.dicoding.tugas_akhir.data.remote.response.ShipScheduleResponse
import com.dicoding.tugas_akhir.domain.model.TicketClassOption
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class FakeRemoteDataSource private constructor() {

    private val bookings = mutableListOf<BookingResponse>()

    suspend fun getUpcomingSchedules(): List<ShipSchedule> {
        delay(900)

        return dummyShipSchedules
    }

    suspend fun getScheduleById(scheduleId: String): ShipSchedule? {
        delay(600)

        return dummyShipSchedules.find { schedule ->
            isSameScheduleId(
                dataId = schedule.id.toString(),
                requestedId = scheduleId
            )
        }
    }

    suspend fun searchSchedules(
        origin: String,
        destination: String,
        date: String,
    ): List<ShipScheduleResponse> {
        delay(900)

        return DummyShipScheduleApiData.schedules.filter { schedule ->
            val isOriginMatch = schedule.origin.contains(origin, ignoreCase = true)
            val isDestinationMatch = schedule.destination.contains(destination, ignoreCase = true)
            val isDateMatch = schedule.departureDate == date

            isOriginMatch && isDestinationMatch && isDateMatch
        }
    }

    suspend fun getScheduleDetail(scheduleId: String): ShipScheduleResponse? {
        delay(600)

        return DummyShipScheduleApiData.schedules.find { schedule ->
            isSameScheduleId(
                dataId = schedule.id,
                requestedId = scheduleId
            )
        }
    }

    suspend fun getTicketClassOptions(scheduleId: String): List<TicketClassOption> {
        delay(600)

        val schedule = getScheduleDetail(scheduleId) ?: return emptyList()

        return listOf(
            TicketClassOption(
                id = "economy",
                name = "Ekonomi",
                price = schedule.economyPrice,
                description = "Pilihan hemat untuk perjalanan antar pulau.",
            ),
            TicketClassOption(
                id = "business",
                name = "Bisnis",
                price = schedule.businessPrice,
                description = "Tempat duduk lebih nyaman dengan fasilitas tambahan.",
            ),
            TicketClassOption(
                id = "first_class",
                name = "Kelas I",
                price = schedule.firstClassPrice,
                description = "Pilihan terbaik dengan kenyamanan lebih maksimal.",
            ),
        )
    }

    suspend fun createBooking(request: CreateBookingRequest): BookingResponse {
        delay(1200)

        val schedule = getScheduleDetail(request.scheduleId)
            ?: throw IllegalArgumentException("Jadwal tidak ditemukan")

        val ticketClass = getTicketClassOptions(request.scheduleId)
            .find { it.id == request.ticketClassId }
            ?: throw IllegalArgumentException("Kelas tiket tidak ditemukan")

        val passengers = request.passengers.mapIndexed { index, passenger ->
            PassengerResponse(
                id = "PSG-${System.currentTimeMillis()}-$index",
                fullName = passenger.fullName,
                nik = passenger.nik,
                phoneNumber = passenger.phoneNumber,
                birthDate = passenger.birthDate,
                gender = passenger.gender,
            )
        }

        val adminFee = 5000
        val totalTicketPrice = ticketClass.price * passengers.size
        val totalPrice = totalTicketPrice + adminFee

        val booking = BookingResponse(
            id = "BKG-${UUID.randomUUID().toString().take(8).uppercase()}",
            scheduleId = schedule.id,
            shipName = schedule.shipName,
            origin = schedule.origin,
            destination = schedule.destination,
            departureDate = schedule.departureDate,
            departureTime = schedule.departureTime,
            ticketClassName = ticketClass.name,
            ticketPrice = ticketClass.price,
            passengerCount = passengers.size,
            passengers = passengers,
            adminFee = adminFee,
            totalPrice = totalPrice,
            status = "Menunggu Pembayaran",
            createdAt = getCurrentDateTime(),
            paymentDeadline = "30 menit",
        )

        bookings.add(0, booking)
        return booking
    }

    suspend fun getBookingDetail(bookingId: String): BookingResponse? {
        delay(600)

        return bookings.find { booking ->
            booking.id == bookingId
        }
    }

    private fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
        return formatter.format(Date())
    }

    private fun isSameScheduleId(
        dataId: String,
        requestedId: String,
    ): Boolean {
        val cleanDataId = dataId.trim()
        val cleanRequestedId = requestedId.trim()

        val dataNumber = cleanDataId.filter { it.isDigit() }.toIntOrNull()
        val requestedNumber = cleanRequestedId.filter { it.isDigit() }.toIntOrNull()

        return cleanDataId == cleanRequestedId ||
                dataNumber != null &&
                requestedNumber != null &&
                dataNumber == requestedNumber
    }

    companion object {
        @Volatile
        private var INSTANCE: FakeRemoteDataSource? = null

        fun getInstance(): FakeRemoteDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = FakeRemoteDataSource()
                INSTANCE = instance
                instance
            }
        }
    }
}