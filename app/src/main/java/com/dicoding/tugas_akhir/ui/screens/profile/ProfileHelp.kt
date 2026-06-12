package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Help
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.profile.*
import com.dicoding.tugas_akhir.ui.theme.Background

@Composable
fun HelpScreen(
    onHelpItemClick: (Int) -> Unit
) {
    val helpItems = listOf(
        "Cara mencari jadwal kapal",
        "Cara memesan tiket",
        "Metode pembayaran",
        "Refund dan Reschedule",
        "Data penumpang",
        "Bantuan kendala aplikasi"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            ProfileTextField(
                label = "Cari Bantuan",
                value = "",
                onValueChange = {},
                placeholder = "Cari topik bantuan"
            )
        }

        helpItems.forEachIndexed { index, title ->
            item {
                DetailMenuItem(
                    icon = Icons.Outlined.Help,
                    title = title,
                    description = "Panduan terkait $title",
                    onClick = {
                        onHelpItemClick(index)
                    }
                )
            }
        }

        item {
            InfoNote(
                text = "Butuh bantuan lebih lanjut? Hubungi layanan bantuan melalui menu kontak bantuan aplikasi."
            )
        }
    }
}