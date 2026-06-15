package com.dicoding.tugas_akhir.ui.components.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.domain.model.SavedPassenger

@Composable
fun SavedPassengerBookingActionCard(
    savedPassengerCount: Int,
    saveToPassengerData: Boolean,
    onPickSavedPassengerClick: () -> Unit,
    onSaveCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE3EAF2),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Text(
                        text = "Data Penumpang Tersimpan",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF102A43),
                    )

                    Text(
                        text = if (savedPassengerCount > 0) {
                            "Pilih data yang sudah tersimpan agar form terisi otomatis."
                        } else {
                            "Belum ada data tersimpan. Kamu bisa simpan data ini untuk booking berikutnya."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF627D98),
                    )
                }

                OutlinedButton(
                    onClick = onPickSavedPassengerClick,
                    enabled = savedPassengerCount > 0,
                ) {
                    Text("Ambil Data")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSaveCheckedChange(!saveToPassengerData)
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = saveToPassengerData,
                    onCheckedChange = onSaveCheckedChange,
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = "Simpan data penumpang ini",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF102A43),
                    )

                    Text(
                        text = "Data akan muncul di Profil > Data Penumpang.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF627D98),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPassengerPickerSheet(
    passengers: List<SavedPassenger>,
    onPassengerClick: (SavedPassenger) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Pilih Data Penumpang",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF102A43),
            )

            Text(
                text = "Data yang dipilih akan mengisi form penumpang yang sedang aktif.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF627D98),
            )

            passengers.forEach { passenger ->
                SavedPassengerOptionCard(
                    passenger = passenger,
                    onClick = {
                        onPassengerClick(passenger)
                    },
                )
            }
        }
    }
}

@Composable
private fun SavedPassengerOptionCard(
    passenger: SavedPassenger,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7FAFC),
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE3EAF2),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFEAF4FF),
                        shape = RoundedCornerShape(14.dp),
                    )
                    .padding(10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.People,
                    contentDescription = null,
                    tint = Color(0xFF1976D2),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = passenger.fullName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF102A43),
                )

                Text(
                    text = "${maskNik(passenger.nik)} • ${passenger.gender}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF627D98),
                )

                Text(
                    text = passenger.phoneNumber,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF829AB1),
                )
            }
        }
    }
}

private fun maskNik(nik: String): String {
    return if (nik.length >= 6) {
        nik.take(6) + "xxxxxxxxxx"
    } else {
        nik
    }
}