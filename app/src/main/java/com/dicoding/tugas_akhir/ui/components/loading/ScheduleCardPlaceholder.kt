package com.dicoding.tugas_akhir.ui.components.loading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun ScheduleCardPlaceholder(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .shimmerEffect(cornerRadius = 14)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(130.dp)
                                .height(18.dp)
                                .shimmerEffect()
                        )

                        Box(
                            modifier = Modifier
                                .width(180.dp)
                                .height(14.dp)
                                .shimmerEffect()
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .width(74.dp)
                        .height(28.dp)
                        .shimmerEffect(cornerRadius = 50)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .shimmerEffect(cornerRadius = 1)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .height(13.dp)
                            .shimmerEffect()
                    )

                    Box(
                        modifier = Modifier
                            .width(86.dp)
                            .height(16.dp)
                            .shimmerEffect()
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .height(13.dp)
                            .shimmerEffect()
                    )

                    Box(
                        modifier = Modifier
                            .width(96.dp)
                            .height(16.dp)
                            .shimmerEffect()
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(14.dp)
                        .shimmerEffect()
                )

                Box(
                    modifier = Modifier
                        .width(118.dp)
                        .height(20.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}