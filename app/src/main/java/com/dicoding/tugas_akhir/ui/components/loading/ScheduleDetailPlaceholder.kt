package com.dicoding.tugas_akhir.ui.components.loading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun ScheduleDetailPlaceholder(
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            top = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            HeroPlaceholderCard()
        }

        item {
            InfoPlaceholderCard(rowCount = 6)
        }

        item {
            InfoPlaceholderCard(rowCount = 3)
        }

        item {
            TicketClassPlaceholderCard()
        }

        item {
            FacilityPlaceholderCard()
        }

        item {
            InfoBoxPlaceholderCard()
        }
    }
}

@Composable
private fun HeroPlaceholderCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .shimmerEffect(cornerRadius = 14)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(18.dp)
                        .shimmerEffect()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(14.dp)
                        .shimmerEffect()
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .width(74.dp)
                    .height(28.dp)
                    .shimmerEffect(cornerRadius = 50)
            )
        }
    }
}

@Composable
private fun InfoPlaceholderCard(
    rowCount: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            repeat(rowCount) {
                PlaceholderInfoRow()
            }
        }
    }
}

@Composable
private fun PlaceholderInfoRow() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .shimmerEffect(cornerRadius = 50)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(14.dp)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.width(18.dp))

        Box(
            modifier = Modifier
                .width(110.dp)
                .height(14.dp)
                .shimmerEffect()
        )
    }
}

@Composable
private fun TicketClassPlaceholderCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .height(18.dp)
                    .shimmerEffect()
            )

            repeat(3) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(110.dp)
                                .height(15.dp)
                                .shimmerEffect()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .height(12.dp)
                                .shimmerEffect()
                        )

                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(12.dp)
                                .shimmerEffect()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(92.dp)
                            .height(16.dp)
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@Composable
private fun FacilityPlaceholderCard() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(90.dp)
                .height(18.dp)
                .shimmerEffect()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .width(96.dp)
                        .height(30.dp)
                        .shimmerEffect(cornerRadius = 50)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .width(118.dp)
                        .height(30.dp)
                        .shimmerEffect(cornerRadius = 50)
                )
            }
        }
    }
}

@Composable
private fun InfoBoxPlaceholderCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(16.dp)
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(14.dp)
                    .shimmerEffect()
            )
        }
    }
}