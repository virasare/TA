package com.dicoding.tugas_akhir.ui.components.notification

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun NotificationCardPlaceholder(
    modifier: Modifier = Modifier,
) {
    val shimmerBrush = rememberShimmerBrush()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        brush = shimmerBrush,
                        shape = RoundedCornerShape(16.dp),
                    )
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(14.dp)
                        .background(
                            brush = shimmerBrush,
                            shape = RoundedCornerShape(8.dp),
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .background(
                            brush = shimmerBrush,
                            shape = RoundedCornerShape(8.dp),
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(12.dp)
                        .background(
                            brush = shimmerBrush,
                            shape = RoundedCornerShape(8.dp),
                        )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.28f)
                        .height(10.dp)
                        .background(
                            brush = shimmerBrush,
                            shape = RoundedCornerShape(8.dp),
                        )
                )
            }
        }
    }
}

@Composable
private fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "notification_shimmer")

    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Restart,
        ),
        label = "notification_shimmer_translate",
    )

    return Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        ),
        start = Offset.Zero,
        end = Offset(
            x = translateAnim.value,
            y = translateAnim.value,
        ),
    )
}