package com.dicoding.tugas_akhir.ui.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
fun EditProfilePlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.32f)
                    .height(18.dp)
                    .shimmerEffect(),
            )

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .shimmerEffect(),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(18.dp)
                    .shimmerEffect(),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(14.dp)
                    .shimmerEffect(),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .shimmerEffect(),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.42f)
                    .height(18.dp)
                    .shimmerEffect(),
            )

            repeat(4) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect(),
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .shimmerEffect(),
            )
        }
    }
}