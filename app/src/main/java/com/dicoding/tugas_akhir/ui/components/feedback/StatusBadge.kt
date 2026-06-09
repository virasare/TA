package com.dicoding.tugas_akhir.ui.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.theme.Error
import com.dicoding.tugas_akhir.ui.theme.ErrorLight
import com.dicoding.tugas_akhir.ui.theme.Info
import com.dicoding.tugas_akhir.ui.theme.InfoLight
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral100
import com.dicoding.tugas_akhir.ui.theme.Success
import com.dicoding.tugas_akhir.ui.theme.SuccessLight
import com.dicoding.tugas_akhir.ui.theme.Warning
import com.dicoding.tugas_akhir.ui.theme.WarningLight

enum class BadgeVariant {
    Success,
    Info,
    Warning,
    Error,
    Neutral
}

@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    variant: BadgeVariant = BadgeVariant.Success
) {
    val backgroundColor: Color
    val contentColor: Color

    when (variant) {
        BadgeVariant.Success -> {
            backgroundColor = SuccessLight
            contentColor = Success
        }

        BadgeVariant.Info -> {
            backgroundColor = InfoLight
            contentColor = Info
        }

        BadgeVariant.Warning -> {
            backgroundColor = WarningLight
            contentColor = Warning
        }

        BadgeVariant.Error -> {
            backgroundColor = ErrorLight
            contentColor = Error
        }

        BadgeVariant.Neutral -> {
            backgroundColor = Neutral100
            contentColor = Neutral500
        }
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = contentColor,
        modifier = modifier
            .heightIn(min = 24.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(
                PaddingValues(
                    horizontal = 10.dp,
                    vertical = 4.dp
                )
            )
    )
}

@Preview(
    showBackground = true,
    widthDp = 360
)
@Composable
fun StatusBadgePreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatusBadge(
            text = "Aktif",
            variant = BadgeVariant.Success
        )

        StatusBadge(
            text = "Menunggu Pembayaran",
            variant = BadgeVariant.Info
        )

        StatusBadge(
            text = "Refund Diproses",
            variant = BadgeVariant.Warning
        )

        StatusBadge(
            text = "Dibatalkan",
            variant = BadgeVariant.Error
        )

        StatusBadge(
            text = "Selesai",
            variant = BadgeVariant.Neutral
        )
    }
}