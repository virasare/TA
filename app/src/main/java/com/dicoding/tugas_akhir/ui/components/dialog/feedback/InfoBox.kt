package com.dicoding.tugas_akhir.ui.components.dialog.feedback

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.R
import com.dicoding.tugas_akhir.ui.theme.Error
import com.dicoding.tugas_akhir.ui.theme.ErrorLight
import com.dicoding.tugas_akhir.ui.theme.Info
import com.dicoding.tugas_akhir.ui.theme.InfoLight
import com.dicoding.tugas_akhir.ui.theme.Neutral100
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Success
import com.dicoding.tugas_akhir.ui.theme.SuccessLight
import com.dicoding.tugas_akhir.ui.theme.Warning
import com.dicoding.tugas_akhir.ui.theme.WarningLight

enum class InfoBoxVariant {
    Info,
    Success,
    Warning,
    Error,
    Neutral
}

@Composable
fun InfoBox(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    variant: InfoBoxVariant = InfoBoxVariant.Info,
    @DrawableRes iconRes: Int? = null
) {
    val backgroundColor: Color
    val borderColor: Color
    val iconColor: Color

    when (variant) {
        InfoBoxVariant.Info -> {
            backgroundColor = InfoLight
            borderColor = Info
            iconColor = Info
        }

        InfoBoxVariant.Success -> {
            backgroundColor = SuccessLight
            borderColor = Success
            iconColor = Success
        }

        InfoBoxVariant.Warning -> {
            backgroundColor = WarningLight
            borderColor = Warning
            iconColor = Warning
        }

        InfoBoxVariant.Error -> {
            backgroundColor = ErrorLight
            borderColor = Error
            iconColor = Error
        }

        InfoBoxVariant.Neutral -> {
            backgroundColor = Neutral100
            borderColor = Neutral500
            iconColor = Neutral500
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                border = BorderStroke(1.dp, borderColor.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Neutral700
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Neutral500
            )
        }
    }
}

@Preview(
    name = "InfoBox Preview",
    showBackground = true,
    widthDp = 360,
    heightDp = 520
)
@Composable
private fun InfoBoxPreview() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoBox(
                    title = "Informasi Jadwal",
                    description = "Jadwal kapal dapat berubah sewaktu-waktu mengikuti kondisi pelabuhan.",
                    variant = InfoBoxVariant.Info,
                    iconRes = R.drawable.ic_info_circle
                )

                InfoBox(
                    title = "Pembayaran Berhasil",
                    description = "Tiket berhasil diterbitkan dan dapat dilihat pada halaman Tiket Saya.",
                    variant = InfoBoxVariant.Success,
                    iconRes = R.drawable.ic_check_circle
                )

                InfoBox(
                    title = "Selesaikan Pembayaran",
                    description = "Pembayaran harus diselesaikan sebelum batas waktu yang ditentukan.",
                    variant = InfoBoxVariant.Warning,
                    iconRes = R.drawable.ic_warning
                )

                InfoBox(
                    title = "Pembayaran Gagal",
                    description = "Terjadi kesalahan saat memproses pembayaran. Silakan coba lagi.",
                    variant = InfoBoxVariant.Error,
                    iconRes = R.drawable.ic_warning
                )

                InfoBox(
                    title = "Tidak Ada Notifikasi",
                    description = "Belum ada informasi terbaru terkait pesanan tiket Anda.",
                    variant = InfoBoxVariant.Neutral,
                    iconRes = R.drawable.ic_notifikasi
                )
            }
        }
    }
}

