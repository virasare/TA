package com.dicoding.tugas_akhir.ui.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.ui.components.lottie.LottieStateView
import com.dicoding.tugas_akhir.ui.components.notification.AppNotificationCard
import com.dicoding.tugas_akhir.ui.components.notification.NotificationCardPlaceholder
import com.dicoding.tugas_akhir.ui.components.notification.NotificationFilter
import com.dicoding.tugas_akhir.ui.components.notification.NotificationFilterSection
import com.dicoding.tugas_akhir.ui.components.notification.NotificationSummaryCard
import com.dicoding.tugas_akhir.ui.viewmodel.NotificationViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

@Composable
fun NotificationScreen(
    onNotificationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val unreadCount by viewModel.unreadCount.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var selectedFilter by remember {
        mutableStateOf(NotificationFilter.ALL)
    }

    val filteredNotifications = when (selectedFilter) {
        NotificationFilter.ALL -> notifications
        NotificationFilter.UNREAD -> notifications.filter { !it.isRead }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 10.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NotificationSummaryCard(
                unreadCount = unreadCount,
                onMarkAllAsRead = {
                    viewModel.markAllAsRead()
                },
            )

            NotificationFilterSection(
                selectedFilter = selectedFilter,
                onFilterSelected = { filter ->
                    selectedFilter = filter
                },
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 4.dp,
                bottom = 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            when {
                isLoading -> {
                    items(5) {
                        NotificationCardPlaceholder()
                    }
                }

                filteredNotifications.isEmpty() -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 80.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            LottieStateView(
                                animationFile = "empty.json",
                                title = "Belum ada notifikasi",
                                message = "Notifikasi pembayaran, tiket, dan jadwal kapal akan muncul di sini.",
                            )
                        }
                    }
                }

                else -> {
                    items(
                        items = filteredNotifications,
                        key = { notification -> notification.id },
                    ) { notification ->
                        AppNotificationCard(
                            notification = notification,
                            onClick = {
                                viewModel.markAsRead(notification.id)
                                onNotificationClick(notification.id)
                            },
                            onDeleteClick = {
                                viewModel.deleteNotification(notification.id)
                            },
                        )
                    }
                }
            }
        }
    }
}