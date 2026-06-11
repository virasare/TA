package com.dicoding.tugas_akhir.data.dummy

enum class NotificationType {
    PaymentSuccess,
    DepartureReminder,
    ScheduleChanged,
    RefundProcess
}

data class AppNotification(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType,
    val isRead: Boolean,
    val bookingCode: String? = null,
    val shipName: String? = null,
    val originPort: String? = null,
    val destinationPort: String? = null,
    val departureSchedule: String? = null,
    val status: String? = null
)

val dummyNotifications = listOf(
    AppNotification(
        id = 1,
        title = "Pembayaran Berhasil",
        message = "Pembayaran tiket KM Dharma Rucitra VIII telah dikonfirmasi.",
        time = "2 menit lalu",
        type = NotificationType.PaymentSuccess,
        isRead = false,
        bookingCode = "NKP12345",
        shipName = "KM Dharma Rucitra VIII",
        originPort = "Pelabuhan IPPI, Ende",
        destinationPort = "Pelabuhan Tanjung Perak, Surabaya",
        departureSchedule = "12 Juli 2026, 18.00 WITA",
        status = "Pembayaran Berhasil"
    ),
    AppNotification(
        id = 2,
        title = "Pengingat Keberangkatan",
        message = "Kapal Anda berangkat besok pukul 18.00 WITA.",
        time = "1 hari lalu",
        type = NotificationType.DepartureReminder,
        isRead = true,
        bookingCode = "NKP12345",
        shipName = "KM Dharma Rucitra VIII",
        originPort = "Pelabuhan IPPI, Ende",
        destinationPort = "Pelabuhan Tanjung Perak, Surabaya",
        departureSchedule = "12 Juli 2026, 18.00 WITA",
        status = "Aktif"
    ),
    AppNotification(
        id = 3,
        title = "Jadwal Berubah",
        message = "Terdapat perubahan jadwal keberangkatan.",
        time = "2 hari lalu",
        type = NotificationType.ScheduleChanged,
        isRead = true,
        bookingCode = "NKP22341",
        shipName = "KM Nusa Bahari",
        originPort = "Pelabuhan Ende",
        destinationPort = "Pelabuhan Benoa, Denpasar",
        departureSchedule = "14 Juli 2026, 09.00 WITA",
        status = "Jadwal Berubah"
    ),
    AppNotification(
        id = 4,
        title = "Refund Diproses",
        message = "Pengajuan refund sedang diproses.",
        time = "3 hari lalu",
        type = NotificationType.RefundProcess,
        isRead = true,
        bookingCode = "NKP55210",
        shipName = "KM Laut Flores",
        originPort = "Pelabuhan Tenau Kupang",
        destinationPort = "Pelabuhan Tanjung Perak, Surabaya",
        departureSchedule = "15 Juli 2026, 20.00 WITA",
        status = "Refund Diproses"
    )
)