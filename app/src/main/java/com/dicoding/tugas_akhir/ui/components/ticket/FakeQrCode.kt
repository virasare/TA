package com.dicoding.tugas_akhir.ui.components.ticket

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@Composable
fun FakeQrCode(
    value: String,
    modifier: Modifier = Modifier,
) {
    val seed = remember(value) {
        value.hashCode().absoluteValue
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp),
    ) {
        Canvas(
            modifier = Modifier.size(160.dp),
        ) {
            val modules = 21
            val cellSize = size.minDimension / modules

            for (row in 0 until modules) {
                for (col in 0 until modules) {
                    val isFinderArea =
                        row < 7 && col < 7 ||
                                row < 7 && col > 13 ||
                                row > 13 && col < 7

                    if (!isFinderArea) {
                        val shouldDraw = ((row * 31 + col * 17 + seed) % 5) < 2

                        if (shouldDraw) {
                            drawRect(
                                color = Color.Black,
                                topLeft = Offset(
                                    x = col * cellSize,
                                    y = row * cellSize,
                                ),
                                size = Size(cellSize, cellSize),
                            )
                        }
                    }
                }
            }

            drawFinderPattern(
                row = 0,
                col = 0,
                cellSize = cellSize,
            )

            drawFinderPattern(
                row = 0,
                col = 14,
                cellSize = cellSize,
            )

            drawFinderPattern(
                row = 14,
                col = 0,
                cellSize = cellSize,
            )
        }
    }
}

private fun DrawScope.drawFinderPattern(
    row: Int,
    col: Int,
    cellSize: Float,
) {
    drawRect(
        color = Color.Black,
        topLeft = Offset(
            x = col * cellSize,
            y = row * cellSize,
        ),
        size = Size(
            width = cellSize * 7,
            height = cellSize * 7,
        ),
    )

    drawRect(
        color = Color.White,
        topLeft = Offset(
            x = (col + 1) * cellSize,
            y = (row + 1) * cellSize,
        ),
        size = Size(
            width = cellSize * 5,
            height = cellSize * 5,
        ),
    )

    drawRect(
        color = Color.Black,
        topLeft = Offset(
            x = (col + 2) * cellSize,
            y = (row + 2) * cellSize,
        ),
        size = Size(
            width = cellSize * 3,
            height = cellSize * 3,
        ),
    )
}