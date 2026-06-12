package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.profile.*
import com.dicoding.tugas_akhir.ui.theme.Background

@Composable
fun ThemeSettingScreen(
    onSaveClick: () -> Unit
) {
    var selectedTheme by remember {
        mutableStateOf("Terang")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileFormCard {
                    ChoiceSection(
                        title = "Pilih Tema",
                        options = listOf("Terang", "Gelap", "Ikuti sistem"),
                        selectedOption = selectedTheme,
                        onOptionSelected = {
                            selectedTheme = it
                        }
                    )
                }
            }
        }

        BottomActionButton(
            text = "Simpan",
            onClick = onSaveClick
        )
    }
}