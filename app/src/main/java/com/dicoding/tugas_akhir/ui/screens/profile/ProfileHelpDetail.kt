package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.components.profile.ProfileFormCard
import com.dicoding.tugas_akhir.ui.components.profile.StepCard
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings

@Composable
fun ProfileHelpDetailScreen(
    type: String,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current

    val title = when (type) {
        "schedule" -> "Panduan Cari Jadwal"
        "booking" -> "Panduan Pemesanan Tiket"
        "payment" -> "Panduan Pembayaran"
        "profile" -> "Panduan Akun dan Data Penumpang"
        else -> strings.helpTitle
    }

    val steps = when (type) {
        "schedule" -> listOf(
            "Buka halaman Beranda atau Jadwal.",
            "Pilih pelabuhan asal dan tujuan.",
            "Pilih tanggal keberangkatan.",
            "Tekan tombol cari jadwal.",
            "Lihat daftar kapal yang tersedia.",
        )

        "booking" -> listOf(
            "Pilih jadwal kapal yang diinginkan.",
            "Tekan pesan tiket.",
            "Pilih kelas tiket dan jumlah penumpang.",
            "Isi data penumpang secara manual atau ambil dari data tersimpan.",
            "Periksa ringkasan pesanan sebelum melanjutkan.",
        )

        "payment" -> listOf(
            "Pilih metode pembayaran yang tersedia.",
            "Ikuti instruksi pembayaran.",
            "Selesaikan pembayaran sebelum batas waktu.",
            "Tekan cek status pembayaran.",
            "Jika berhasil, e-ticket akan diterbitkan.",
        )

        "profile" -> listOf(
            "Buka halaman Profil.",
            "Pilih Edit Profil untuk mengubah nama atau foto.",
            "Pilih Data Penumpang untuk menyimpan data penumpang.",
            "Data penumpang tersimpan dapat digunakan kembali saat booking.",
        )

        else -> listOf(
            "Pilih fitur yang ingin digunakan.",
            "Ikuti instruksi yang tersedia pada halaman aplikasi.",
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            InfoNote(
                title = title,
                text = "Ikuti langkah-langkah berikut agar proses penggunaan aplikasi lebih mudah.",
            )
        }

        item {
            ProfileFormCard {
                steps.forEachIndexed { index, step ->
                    StepCard(
                        number = index + 1,
                        text = step,
                    )
                }
            }
        }
    }
}