package com.dicoding.tugas_akhir.ui.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun PassengerDataPlaceholder(
    modifier: Modifier = Modifier,
    itemCount: Int = 3,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        repeat(itemCount) {
            PassengerDataPlaceholderItem()
        }
    }
}

@Composable
private fun PassengerDataPlaceholderItem(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .shimmerEffect(),
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
            )
        }
    }
}