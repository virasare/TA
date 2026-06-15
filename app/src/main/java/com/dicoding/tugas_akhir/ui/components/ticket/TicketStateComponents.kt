package com.dicoding.tugas_akhir.ui.components.ticket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun TicketEmptyState(
    title: String = "Belum Ada Pesanan",
    description: String = "Pesanan tiket kapal kamu akan muncul di sini setelah berhasil dibuat.",
    buttonText: String = "Cari Jadwal",
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = White,
            ),
            border = BorderStroke(1.dp, Neutral200),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .background(
                            color = Primary3,
                            shape = RoundedCornerShape(24.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ConfirmationNumber,
                        contentDescription = null,
                        tint = Primary2,
                        modifier = Modifier.size(38.dp),
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Neutral700,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                    textAlign = TextAlign.Center,
                )

                OutlinedButton(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = buttonText,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
fun TicketLoadingState(
    title: String = "Memuat Pesanan",
    description: String = "Mohon tunggu sebentar, data pesanan kamu sedang disiapkan.",
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = White,
            ),
            border = BorderStroke(1.dp, Neutral200),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(34.dp),
                    color = Primary2,
                    strokeWidth = 3.dp,
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Neutral700,
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral500,
                    )
                }
            }
        }
    }
}

@Composable
fun TicketErrorState(
    message: String,
    buttonText: String = "Coba Lagi",
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = White,
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(42.dp),
                )

                Text(
                    text = "Gagal Memuat Data",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Neutral700,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = Neutral500,
                    textAlign = TextAlign.Center,
                )

                OutlinedButton(
                    onClick = onRetryClick,
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = buttonText,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}