package com.dicoding.tugas_akhir.data.remote.datasource

import com.dicoding.tugas_akhir.data.dummy.DummyShipScheduleApiData
import com.dicoding.tugas_akhir.data.remote.request.CreateBookingRequest
import com.dicoding.tugas_akhir.data.remote.request.CreatePaymentRequest
import com.dicoding.tugas_akhir.data.remote.response.BookingResponse
import com.dicoding.tugas_akhir.data.remote.response.ETicketResponse
import com.dicoding.tugas_akhir.data.remote.response.PassengerResponse
import com.dicoding.tugas_akhir.data.remote.response.PaymentMethodResponse
import com.dicoding.tugas_akhir.data.remote.response.PaymentResponse
import com.dicoding.tugas_akhir.data.remote.response.ShipScheduleResponse
import com.dicoding.tugas_akhir.domain.model.TicketClassOption
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule as DummyShipSchedule
import com.dicoding.tugas_akhir.data.dummy.dummyShipSchedules
import com.dicoding.tugas_akhir.data.dummy.filterUpcomingSchedules
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus

class FakeRemoteDataSource private constructor() {

    private val bookings = mutableListOf<BookingResponse>()
    private val payments = mutableListOf<PaymentResponse>()

    private fun getDummyScheduleResponses(
        includePastSchedules: Boolean = false,
    ): List<ShipScheduleResponse> {
        val schedules = if (includePastSchedules) {
            dummyShipSchedules
        } else {
            dummyShipSchedules.filterUpcomingSchedules()
        }

        return schedules.map { it.toScheduleResponse() }
    }

    suspend fun getUpcomingSchedules(): List<ShipScheduleResponse> {
        delay(600)

        return getDummyScheduleResponses()
    }

    suspend fun searchSchedules(
        origin: String,
        destination: String,
        date: String,
    ): List<ShipScheduleResponse> {
        delay(900)

        val normalizedDate = date.toIsoDateOrOriginal()

        return getDummyScheduleResponses().filter { schedule ->
            val isOriginMatch = schedule.origin.contains(origin, ignoreCase = true)
            val isDestinationMatch = schedule.destination.contains(destination, ignoreCase = true)
            val isDateMatch = schedule.departureDate == date ||
                    schedule.departureDate == normalizedDate

            isOriginMatch && isDestinationMatch && isDateMatch
        }
    }

    suspend fun getScheduleDetail(scheduleId: String): ShipScheduleResponse? {
        delay(600)

        getDummyScheduleResponses(includePastSchedules = true).find { schedule ->
            schedule.id == scheduleId
        }?.let { return it }

        val numericId = scheduleId.toScheduleNumber() ?: return DummyShipScheduleApiData.schedules.find {
            it.id == scheduleId
        }

        return dummyShipSchedules.find { schedule ->
            schedule.id == numericId
        }?.toScheduleResponse()
    }

    private fun String.toScheduleNumber(): Int? {
        return when {
            all { it.isDigit() } -> toIntOrNull()
            startsWith("SCH", ignoreCase = true) -> drop(3).toIntOrNull()
            else -> null
        }
    }

    private fun DummyShipSchedule.toScheduleResponse(): ShipScheduleResponse {
        val normalizedRoute = route
            .replace("â†’", "->")
            .replace("Ã¢â€ â€™", "->")
            .replace("\u2192", "->")

        val routeParts = normalizedRoute.split("->").map { it.trim() }
        val origin = routeParts.getOrNull(0).orEmpty()
        val destination = routeParts.getOrNull(1).orEmpty()
        val economyPrice = price.filter { it.isDigit() }.toIntOrNull() ?: 0

        return ShipScheduleResponse(
            id = id.toString(),
            shipName = shipName,
            shipCode = "NS-${id.toString().padStart(3, '0')}",
            origin = origin,
            destination = destination,
            departureDate = departureDate.toIsoDateOrOriginal(),
            departureTime = departureTime,
            arrivalDate = arrivalDate.toIsoDateOrOriginal(),
            arrivalTime = arrivalTime,
            duration = duration,
            economyPrice = economyPrice,
            businessPrice = economyPrice + 150000,
            firstClassPrice = economyPrice + 350000,
            quota = quota.filter { it.isDigit() }.toIntOrNull() ?: 0,
            status = status.toApiStatus(),
            facilities = listOf(
                "Kursi penumpang",
                "Bagasi",
                "Kantin",
                "Toilet",
                "Mushola",
            ),
            description = "Kapal $shipName melayani rute $origin menuju $destination.",
            canRefund = status != ShipScheduleStatus.Unavailable,
            canReschedule = status != ShipScheduleStatus.Unavailable,
        )
    }

    private fun String.toIsoDateOrOriginal(): String {
        val monthMap = mapOf(
            "Jan" to "01",
            "Feb" to "02",
            "Mar" to "03",
            "Apr" to "04",
            "Mei" to "05",
            "Jun" to "06",
            "Jul" to "07",
            "Agu" to "08",
            "Sep" to "09",
            "Okt" to "10",
            "Nov" to "11",
            "Des" to "12",
        )

        val parts = trim().split(" ")
        if (parts.size != 3) return this

        val day = parts[0].padStart(2, '0')
        val month = monthMap[parts[1]] ?: return this
        val year = parts[2]

        return "$year-$month-$day"
    }

    private fun ShipScheduleStatus.toApiStatus(): String {
        return when (this) {
            ShipScheduleStatus.Available -> "Tersedia"
            ShipScheduleStatus.Limited -> "Terbatas"
            ShipScheduleStatus.Unavailable -> "Habis"
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
                description = "Kursi reguler di area penumpang umum. Akses toilet, mushola, kantin, area duduk bersama, dan bagasi kabin. Makanan belum termasuk.",
            ),
            TicketClassOption(
                id = "business",
                name = "Bisnis",
                price = schedule.businessPrice,
                description = "Kursi lebih nyaman dengan ruang duduk lebih lega, area lebih tenang, dan prioritas boarding. Cocok untuk perjalanan menengah hingga jauh.",
            ),
            TicketClassOption(
                id = "first_class",
                name = "Kelas I",
                price = schedule.firstClassPrice,
                description = "Kabin atau ruang istirahat lebih privat dengan fasilitas lebih lengkap. Cocok untuk perjalanan jauh atau penumpang yang ingin lebih nyaman.",
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

    suspend fun getPaymentMethods(): List<PaymentMethodResponse> {
        delay(500)

        return listOf(
            PaymentMethodResponse(
                id = "virtual_account",
                name = "Virtual Account",
                description = "Bayar melalui ATM, mobile banking, atau internet banking.",
            ),
            PaymentMethodResponse(
                id = "qris",
                name = "QRIS",
                description = "Bayar dengan scan QR menggunakan e-wallet atau mobile banking.",
            ),
            PaymentMethodResponse(
                id = "bank_transfer",
                name = "Transfer Bank",
                description = "Transfer manual ke rekening tujuan yang tersedia.",
            ),
        )
    }

    suspend fun createPayment(request: CreatePaymentRequest): PaymentResponse {
        delay(1000)

        val booking = getBookingDetail(request.bookingId)
            ?: throw IllegalArgumentException("Pesanan tidak ditemukan")

        val method = getPaymentMethods()
            .find { it.id == request.paymentMethodId }
            ?: throw IllegalArgumentException("Metode pembayaran tidak ditemukan")

        val payment = PaymentResponse(
            id = "PAY-${UUID.randomUUID().toString().take(8).uppercase()}",
            bookingId = booking.id,
            paymentMethodId = method.id,
            paymentMethodName = method.name,
            totalPrice = booking.totalPrice,
            paymentCode = generatePaymentCode(method.id),
            status = "Menunggu Pembayaran",
            expiredIn = "30 menit",
            instructions = generatePaymentInstructions(method.id),
            createdAt = getCurrentDateTime(),
        )

        payments.removeAll { it.bookingId == booking.id }
        payments.add(0, payment)

        return payment
    }

    suspend fun getPaymentDetail(paymentId: String): PaymentResponse? {
        delay(600)

        return payments.find { payment ->
            payment.id == paymentId
        }
    }

    suspend fun simulatePaymentSuccess(paymentId: String): PaymentResponse {
        delay(1000)

        val index = payments.indexOfFirst { payment ->
            payment.id == paymentId
        }

        if (index == -1) {
            throw IllegalArgumentException("Data pembayaran tidak ditemukan")
        }

        val currentPayment = payments[index]
        val updatedPayment = currentPayment.copy(
            status = "Berhasil",
        )

        payments[index] = updatedPayment
        updateBookingStatus(
            bookingId = updatedPayment.bookingId,
            status = "Aktif",
        )

        return updatedPayment
    }

    suspend fun getMyTickets(): List<BookingResponse> {
        delay(700)

        return bookings
    }

    suspend fun getETicketByBookingId(bookingId: String): ETicketResponse? {
        delay(700)

        return buildETicketResponse(
            bookingId = bookingId,
            paymentId = payments.find { payment ->
                payment.bookingId == bookingId
            }?.id,
        )
    }

    suspend fun getETicketByPaymentId(paymentId: String): ETicketResponse? {
        delay(700)

        val payment = payments.find { item ->
            item.id == paymentId
        } ?: return null

        return buildETicketResponse(
            bookingId = payment.bookingId,
            paymentId = payment.id,
        )
    }

    private fun buildETicketResponse(
        bookingId: String,
        paymentId: String?,
    ): ETicketResponse? {
        val booking = bookings.find { item ->
            item.id == bookingId
        } ?: return null

        return ETicketResponse(
            bookingId = booking.id,
            bookingCode = booking.id.replace("BKG", "NKP"),
            paymentId = paymentId,
            shipName = booking.shipName,
            origin = booking.origin,
            destination = booking.destination,
            departureDate = booking.departureDate,
            departureTime = booking.departureTime,
            ticketClassName = booking.ticketClassName,
            passengers = booking.passengers,
            status = booking.status,
            qrCode = "ETICKET-${booking.id}-${paymentId ?: "UNPAID"}",
            issuedAt = getCurrentDateTime(),
            terminal = "Pelabuhan ${booking.origin}",
            gate = "Gate 2",
            note = "Tunjukkan e-ticket ini kepada petugas pelabuhan saat proses check-in.",
        )
    }

    private fun updateBookingStatus(
        bookingId: String,
        status: String,
    ) {
        val index = bookings.indexOfFirst { booking ->
            booking.id == bookingId
        }

        if (index != -1) {
            bookings[index] = bookings[index].copy(
                status = status,
            )
        }
    }

    private fun generatePaymentCode(methodId: String): String {
        return when (methodId) {
            "virtual_account" -> "8808${System.currentTimeMillis().toString().takeLast(8)}"
            "qris" -> "QRIS-SIMULASI-NUSAKAPAL-${UUID.randomUUID().toString().take(8).uppercase()}"
            "bank_transfer" -> "1234567890"
            else -> "-"
        }
    }

    private fun generatePaymentInstructions(methodId: String): List<String> {
        return when (methodId) {
            "virtual_account" -> listOf(
                "Buka aplikasi mobile banking atau ATM.",
                "Pilih menu Virtual Account.",
                "Masukkan nomor Virtual Account yang tersedia.",
                "Pastikan nominal pembayaran sudah sesuai.",
                "Konfirmasi pembayaran.",
            )

            "qris" -> listOf(
                "Buka aplikasi e-wallet atau mobile banking.",
                "Pilih menu Scan QRIS.",
                "Scan gambar QRIS simulasi yang tersedia.",
                "Pastikan nominal pembayaran sudah sesuai.",
                "Konfirmasi pembayaran.",
            )

            "bank_transfer" -> listOf(
                "Buka aplikasi mobile banking atau ATM.",
                "Pilih menu Transfer Bank ke BCA.",
                "Masukkan rekening tujuan 1234567890 a.n. PT NusaKapal Indonesia.",
                "Masukkan nominal pembayaran sesuai total pesanan.",
                "Pastikan nama penerima dan nominal sudah benar.",
                "Simpan bukti pembayaran.",
            )

            else -> listOf(
                "Ikuti instruksi pembayaran yang tersedia.",
            )
        }
    }

    private fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
        return formatter.format(Date())
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
