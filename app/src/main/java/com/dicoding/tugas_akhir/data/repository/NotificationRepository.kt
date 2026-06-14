package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.data.local.room.dao.NotificationDao
import com.dicoding.tugas_akhir.data.mapper.DataMapper
import com.dicoding.tugas_akhir.domain.model.AppNotification
import com.dicoding.tugas_akhir.domain.model.NotificationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class NotificationRepository private constructor(
    private val notificationDao: NotificationDao,
) {

    fun getNotifications(): Flow<List<AppNotification>> {
        return notificationDao.getNotifications().map { entities ->
            entities.map {
                DataMapper.mapNotificationEntityToDomain(it)
            }
        }
    }

    fun getUnreadCount(): Flow<Int> {
        return notificationDao.getUnreadNotificationCount()
    }

    suspend fun seedInitialNotifications() {
        val currentNotifications = notificationDao.getNotifications().first()

        if (currentNotifications.isNotEmpty()) return

        val now = System.currentTimeMillis()

        val dummyNotifications = listOf(
            AppNotification(
                id = "NOTIF-001",
                title = "Pembayaran Berhasil",
                message = "Pembayaran tiket berhasil dikonfirmasi.",
                type = NotificationType.PAYMENT,
                createdAt = now - 5 * 60 * 1000,
                isRead = false,
            ),
            AppNotification(
                id = "NOTIF-002",
                title = "Tiket Berhasil Diterbitkan",
                message = "E-ticket kamu sudah tersedia dan dapat dilihat pada halaman Tiket Saya.",
                type = NotificationType.TICKET,
                createdAt = now - 30 * 60 * 1000,
                isRead = false,
            ),
            AppNotification(
                id = "NOTIF-003",
                title = "Booking Menunggu Pembayaran",
                message = "Selesaikan pembayaran sebelum batas waktu berakhir.",
                type = NotificationType.PAYMENT,
                createdAt = now - 2 * 60 * 60 * 1000,
                isRead = true,
            ),
            AppNotification(
                id = "NOTIF-004",
                title = "Informasi Sistem",
                message = "Pastikan data penumpang sesuai identitas sebelum melakukan pemesanan.",
                type = NotificationType.INFO,
                createdAt = now - 24 * 60 * 60 * 1000,
                isRead = true,
            ),
        )

        notificationDao.insertNotifications(
            dummyNotifications.map {
                DataMapper.mapNotificationDomainToEntity(it)
            }
        )
    }

    suspend fun addNotification(notification: AppNotification) {
        notificationDao.insertNotification(
            DataMapper.mapNotificationDomainToEntity(notification)
        )
    }

    suspend fun saveNotification(notification: AppNotification) {
        notificationDao.insertNotification(
            DataMapper.mapNotificationDomainToEntity(notification)
        )
    }

    suspend fun markAsRead(id: String) {
        notificationDao.markAsRead(id)
    }

    suspend fun markAllAsRead() {
        notificationDao.markAllAsRead()
    }

    suspend fun deleteNotification(id: String) {
        notificationDao.deleteNotification(id)
    }

    suspend fun clearNotifications() {
        notificationDao.clearNotifications()
    }

    companion object {
        @Volatile
        private var INSTANCE: NotificationRepository? = null

        fun getInstance(
            notificationDao: NotificationDao,
        ): NotificationRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = NotificationRepository(notificationDao)
                INSTANCE = instance
                instance
            }
        }
    }
}