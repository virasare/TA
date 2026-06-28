package com.dicoding.tugas_akhir.ui.components.dialog.buttons

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator

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
    isLoading: Boolean = false,
    variant: PrimaryButtonVariant = PrimaryButtonVariant.Blue,
    leadingIcon: ImageVector? = null
) {
    val colors = MaterialTheme.colorScheme
    val containerColor = when (variant) {
        PrimaryButtonVariant.Blue -> colors.primary
        PrimaryButtonVariant.Dark -> colors.primary
        PrimaryButtonVariant.Danger -> colors.error
    }
    val contentColor = when (variant) {
        PrimaryButtonVariant.Danger -> colors.onError
        else -> colors.onPrimary
    }

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
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
            contentColor = contentColor,
            disabledContainerColor = if (isLoading) {
                containerColor.copy(alpha = 0.72f)
            } else {
                colors.surfaceVariant
            },
            disabledContentColor = if (isLoading) {
                contentColor
            } else {
                colors.onSurfaceVariant.copy(alpha = 0.62f)
            }
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = contentColor,
            )

            Spacer(modifier = Modifier.width(8.dp))
        } else if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))
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
