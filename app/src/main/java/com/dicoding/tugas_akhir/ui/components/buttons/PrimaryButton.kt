package com.dicoding.tugas_akhir.ui.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.theme.Error
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Primary1
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.White
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import com.dicoding.tugas_akhir.ui.theme.Neutral300

enum class PrimaryButtonVariant {
    Blue,
    Dark,
    Danger
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: PrimaryButtonVariant = PrimaryButtonVariant.Blue,
    leadingIcon: ImageVector? = null
) {
    val containerColor = when (variant) {
        PrimaryButtonVariant.Blue -> Primary2
        PrimaryButtonVariant.Dark -> Primary1
        PrimaryButtonVariant.Danger -> Error
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(
            horizontal = 24.dp,
            vertical = 12.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = White,
            disabledContainerColor = Neutral200,
            disabledContentColor = Neutral300
        )
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 360
)
@Composable
fun PrimaryButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PrimaryButton(
            text = "Cari Jadwal",
            onClick = {}
        )

        PrimaryButton(
            text = "Lanjut Pembayaran",
            onClick = {},
            variant = PrimaryButtonVariant.Dark
        )

        PrimaryButton(
            text = "Batalkan Pesanan",
            onClick = {},
            variant = PrimaryButtonVariant.Danger
        )

        PrimaryButton(
            text = "Button Disabled",
            onClick = {},
            enabled = false
        )
    }
}