package com.dicoding.tugas_akhir.domain.model

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val isRead: Boolean,
    val createdAt: Long,
)

enum class NotificationType {
    PAYMENT,
    TICKET,
    SCHEDULE,
    REFUND,
    RESCHEDULE,
    INFO,
}