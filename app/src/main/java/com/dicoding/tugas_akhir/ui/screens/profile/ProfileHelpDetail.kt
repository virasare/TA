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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.MaterialTheme
import com.dicoding.tugas_akhir.ui.components.profile.StaticInfoItem
import com.dicoding.tugas_akhir.ui.components.profile.StaticInfoSection

@Composable
fun ProfileHelpDetailScreen(
    type: String,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    val content = getHelpDetailContent(type, strings.helpTitle)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            InfoNote(
                title = content.title,
                text = content.intro,
            )
        }

        item {
            ProfileFormCard(
                title = "Langkah Penggunaan",
            ) {
                content.steps.forEachIndexed { index, step ->
                    StepCard(
                        number = index + 1,
                        text = step,
                    )
                }
            }
        }

        item {
            StaticInfoSection(title = "Tips Penting") {
                content.tips.forEach { tip ->
                    StaticInfoItem(
                        title = tip.first,
                        subtitle = tip.second,
                        icon = Icons.Outlined.CheckCircle,
                    )
                }
            }
        }
    }
}

private data class HelpDetailContent(
    val title: String,
    val intro: String,
    val steps: List<String>,
    val tips: List<Pair<String, String>>,
)

private fun getHelpDetailContent(
    type: String,
    defaultTitle: String,
): HelpDetailContent {
    return when (type) {
        "schedule" -> HelpDetailContent(
            title = "Panduan Cari Jadwal",
            intro = "Gunakan pencarian jadwal untuk menemukan kapal berdasarkan pelabuhan asal, tujuan, dan tanggal keberangkatan.",
            steps = listOf(
                "Buka halaman Beranda atau Jadwal.",
                "Pilih pelabuhan asal dan tujuan.",
                "Pilih tanggal keberangkatan.",
                "Tekan tombol cari jadwal.",
                "Pilih jadwal yang sesuai dengan kebutuhan perjalanan.",
            ),
            tips = listOf(
                "Periksa tanggal" to "Pastikan tanggal keberangkatan sudah sesuai sebelum memilih jadwal.",
                "Bandingkan kelas" to "Setiap kapal bisa memiliki kelas dan fasilitas yang berbeda.",
            ),
        )

        "booking" -> HelpDetailContent(
            title = "Panduan Pemesanan Tiket",
            intro = "Pastikan data penumpang benar sebelum membuat pesanan agar e-ticket dapat diterbitkan tanpa kendala.",
            steps = listOf(
                "Pilih jadwal kapal yang tersedia.",
                "Pilih kelas tiket dan jumlah penumpang.",
                "Isi data penumpang secara manual atau gunakan data tersimpan.",
                "Periksa kembali ringkasan pemesanan.",
                "Lanjutkan ke pembayaran jika semua data sudah benar.",
            ),
            tips = listOf(
                "Cek NIK dan nama" to "Nama dan NIK sebaiknya sama dengan identitas penumpang.",
                "Simpan data" to "Gunakan fitur simpan data penumpang untuk mempercepat booking berikutnya.",
            ),
        )

        "payment" -> HelpDetailContent(
            title = "Panduan Pembayaran",
            intro = "Ikuti instruksi pembayaran sesuai metode yang dipilih dan selesaikan sebelum batas waktu berakhir.",
            steps = listOf(
                "Pilih metode pembayaran.",
                "Baca instruksi pembayaran.",
                "Lakukan transfer atau pembayaran QRIS.",
                "Tekan tombol saya sudah bayar.",
                "Jika pembayaran berhasil, e-ticket akan tersedia.",
            ),
            tips = listOf(
                "Periksa nominal" to "Pastikan nominal pembayaran sesuai dengan total tagihan.",
                "Simpan bukti" to "Simpan bukti pembayaran sampai status tiket berhasil.",
            ),
        )

        "profile" -> HelpDetailContent(
            title = "Panduan Akun dan Data Penumpang",
            intro = "Kelola profil dan data penumpang agar proses pemesanan tiket menjadi lebih cepat.",
            steps = listOf(
                "Buka halaman Profil.",
                "Pilih Edit Profil untuk mengubah nama atau foto.",
                "Pilih Data Penumpang untuk menambah data penumpang.",
                "Isi data lengkap termasuk tanggal lahir.",
                "Gunakan data tersimpan saat melakukan booking.",
            ),
            tips = listOf(
                "Lengkapi profil" to "Profil yang lengkap membantu pengalaman penggunaan aplikasi lebih nyaman.",
                "Perbarui data" to "Edit data penumpang jika ada perubahan nomor HP atau informasi pribadi.",
            ),
        )

        else -> HelpDetailContent(
            title = defaultTitle,
            intro = "Pilih topik bantuan yang sesuai dengan kebutuhanmu.",
            steps = listOf(
                "Pilih fitur yang ingin digunakan.",
                "Ikuti instruksi yang tersedia pada halaman aplikasi.",
                "Periksa kembali data sebelum melanjutkan proses penting.",
            ),
            tips = listOf(
                "Gunakan bantuan" to "Buka halaman bantuan saat membutuhkan panduan penggunaan aplikasi.",
            ),
        )
    }
}