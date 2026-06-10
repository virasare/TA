package com.dicoding.tugas_akhir.ui.components.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.Port
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortPickerBottomSheet(
    ports: List<Port>,
    selectedPort: Port?,
    onDismiss: () -> Unit,
    onPortSelected: (Port) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White,
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pilih Pelabuhan",
                    modifier = Modifier.weight(1f),
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Tutup",
                        tint = Neutral500
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                color = Background,
                border = BorderStroke(1.dp, Neutral200)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = Neutral500,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Cari Pelabuhan",
                        color = Neutral500,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ports.forEach { port ->
                    PortItem(
                        port = port,
                        selected = selectedPort == port,
                        onClick = {
                            onPortSelected(port)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PortItem(
    port: Port,
    selected: Boolean,
    onClick: () -> Unit
) {
    val itemBackground = if (selected) Primary3 else White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = itemBackground,
                shape = RoundedCornerShape(0.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(36.dp),
            shape = RoundedCornerShape(10.dp),
            color = if (selected) Primary2 else Primary3
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (selected) {
                        Icons.Outlined.CheckCircle
                    } else {
                        Icons.Outlined.DirectionsBoat
                    },
                    contentDescription = null,
                    tint = if (selected) White else Primary2,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = port.name,
                color = Neutral700,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = port.city,
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}