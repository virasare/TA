package com.dicoding.tugas_akhir.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.Port
import com.dicoding.tugas_akhir.data.dummy.dummyPorts
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral300
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.Tugas_AkhirTheme
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun PortSearchScreen(
    ports: List<Port>,
    onPortSelected: (Port) -> Unit
) {
    var query by remember { mutableStateOf("") }

    val filteredPorts = ports.filter { port ->
        port.name.contains(query, ignoreCase = true) ||
                port.city.contains(query, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            singleLine = true,
            placeholder = {
                Text(
                    text = "Cari pelabuhan atau kota",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Neutral500
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = Neutral500
                )
            },
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                focusedBorderColor = Primary2,
                unfocusedBorderColor = Neutral300,
                cursorColor = Primary2,
                focusedTextColor = Neutral700,
                unfocusedTextColor = Neutral700
            )
        )

        if (filteredPorts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pelabuhan tidak ditemukan",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredPorts) { port ->
                    PortSearchItem(
                        port = port,
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
private fun PortSearchItem(
    port: Port,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = White,
        border = BorderStroke(1.dp, Neutral200),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(12.dp),
                color = Primary3
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DirectionsBoat,
                        contentDescription = null,
                        tint = Primary2,
                        modifier = Modifier.size(22.dp)
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
}


@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
private fun PortSearchScreenPreview() {
    Tugas_AkhirTheme {
        PortSearchScreen(
            ports = dummyPorts,
            onPortSelected = {}
        )
    }
}