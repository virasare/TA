package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugas_akhir.data.repository.NotificationRepository
import com.dicoding.tugas_akhir.domain.model.AppNotification
import com.dicoding.tugas_akhir.domain.model.NotificationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationRepository: NotificationRepository,
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        seedNotifications()
        observeNotifications()
        observeUnreadCount()
    }

    private fun seedNotifications() {
        viewModelScope.launch {
            notificationRepository.seedInitialNotifications()
        }
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            _isLoading.value = true

            notificationRepository.getNotifications().collect { notifications ->
                _notifications.value = notifications
                _isLoading.value = false
            }
        }
    }

    private fun observeUnreadCount() {
        viewModelScope.launch {
            notificationRepository.getUnreadCount().collect { count ->
                _unreadCount.value = count
            }
        }
    }

    fun markAsRead(id: String) {
        viewModelScope.launch {
            notificationRepository.markAsRead(id)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            notificationRepository.markAllAsRead()
        }
    }

    fun deleteNotification(id: String) {
        viewModelScope.launch {
            notificationRepository.deleteNotification(id)
        }
    }

    fun clearNotifications() {
        viewModelScope.launch {
            notificationRepository.clearNotifications()
        }
    }

    fun addNotification(
        title: String,
        message: String,
        type: NotificationType,
    ) {
        viewModelScope.launch {
            notificationRepository.saveNotification(
                AppNotification(
                    id = "NTF-${System.currentTimeMillis()}",
                    title = title,
                    message = message,
                    type = type,
                    isRead = false,
                    createdAt = System.currentTimeMillis(),
                )
            )
        }
    }

    fun addNotificationFromString(
        title: String,
        message: String,
        type: String,
    ) {
        addNotification(
            title = title,
            message = message,
            type = type.toNotificationType(),
        )
    }

    private fun String.toNotificationType(): NotificationType {
        return when (this.lowercase()) {
            "payment" -> NotificationType.PAYMENT
            "ticket" -> NotificationType.TICKET
            "schedule" -> NotificationType.SCHEDULE
            "refund" -> NotificationType.REFUND
            "reschedule" -> NotificationType.RESCHEDULE
            else -> NotificationType.INFO
        }
    }
}