package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.profile.*
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700

@Composable
fun HelpDetailScreen(
    helpId: Int
) {
    val title = when (helpId) {
        0 -> "Cara mencari jadwal kapal"
        1 -> "Cara memesan tiket"
        2 -> "Metode pembayaran"
        3 -> "Refund dan Reschedule"
        4 -> "Data penumpang"
        else -> "Bantuan kendala aplikasi"
    }

    val steps = when (helpId) {
        0 -> listOf(
            "Masuk ke halaman Beranda.",
            "Pilih pelabuhan asal, tujuan, dan tanggal keberangkatan.",
            "Tekan tombol Cari Jadwal.",
            "Pilih jadwal kapal yang sesuai."
        )

        1 -> listOf(
            "Pilih jadwal kapal.",
            "Tekan tombol Pesan Tiket.",
            "Pilih kelas tiket dan jumlah penumpang.",
            "Lengkapi data penumpang.",
            "Lanjutkan ke pembayaran."
        )

        2 -> listOf(
            "Pilih metode pembayaran.",
            "Ikuti instruksi pembayaran.",
            "Selesaikan pembayaran sebelum batas waktu.",
            "Cek status pembayaran."
        )

        3 -> listOf(
            "Masuk ke Detail Pesanan.",
            "Pilih Refund atau Reschedule.",
            "Lengkapi alasan pengajuan.",
            "Tunggu proses verifikasi."
        )

        4 -> listOf(
            "Buka menu Profil.",
            "Pilih Data Penumpang.",
            "Tambah atau ubah data penumpang.",
            "Simpan data untuk pemesanan berikutnya."
        )

        else -> listOf(
            "Periksa koneksi internet.",
            "Tutup dan buka kembali aplikasi.",
            "Pastikan data yang dimasukkan sudah benar.",
            "Hubungi bantuan jika kendala masih terjadi."
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = title,
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Ikuti langkah-langkah berikut untuk menyelesaikan kendala atau memahami fitur aplikasi.",
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        steps.forEachIndexed { index, step ->
            item {
                StepCard(
                    number = index + 1,
                    text = step
                )
            }
        }

        item {
            InfoNote(
                text = "Jika panduan ini belum membantu, silakan hubungi bantuan melalui menu Bantuan."
            )
        }
    }
}