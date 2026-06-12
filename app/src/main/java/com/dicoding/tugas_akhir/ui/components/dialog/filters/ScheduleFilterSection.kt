package com.dicoding.tugas_akhir.ui.components.dialog.filters

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.White

enum class ScheduleFilter(
    val label: String
) {
    All("Semua"),
    Available("Tersedia"),
    Limited("Terbatas"),
    Unavailable("Habis"),
    Cheapest("Termurah")
}

@Composable
fun ScheduleFilterSection(
    selectedFilter: ScheduleFilter,
    onFilterSelected: (ScheduleFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            top = 18.dp,
            bottom = 8.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(ScheduleFilter.entries.toList()) { filter ->
            ScheduleFilterChip(
                text = filter.label,
                selected = selectedFilter == filter,
                onClick = {
                    onFilterSelected(filter)
                }
            )
        }
    }
}

@Composable
private fun ScheduleFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (selected) Primary2 else White
    val contentColor = if (selected) White else Neutral700
    val borderColor = if (selected) Primary2 else Neutral200

    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(50.dp),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            color = contentColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 360
)
@Composable
private fun ScheduleFilterSectionPreview() {
    ScheduleFilterSection(
        selectedFilter = ScheduleFilter.All,
        onFilterSelected = {}
    )
}