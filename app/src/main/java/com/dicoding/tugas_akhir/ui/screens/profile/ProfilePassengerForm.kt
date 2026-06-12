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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.profile.*
import com.dicoding.tugas_akhir.ui.theme.Background

@Composable
fun PassengerProfileFormScreen(
    onSaveClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Perempuan") }

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
                    ProfileTextField(
                        label = "Nama Lengkap",
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = "Masukkan nama lengkap"
                    )

                    ProfileTextField(
                        label = "Nomor Induk Kependudukan",
                        value = nik,
                        onValueChange = { value ->
                            if (value.length <= 16 && value.all { it.isDigit() }) {
                                nik = value
                            }
                        },
                        placeholder = "Masukkan 16 digit NIK",
                        keyboardType = KeyboardType.Number
                    )

                    ProfileTextField(
                        label = "Nomor Telepon",
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = "Masukkan nomor telepon",
                        keyboardType = KeyboardType.Phone
                    )

                    ChoiceSection(
                        title = "Jenis Kelamin",
                        options = listOf("Perempuan", "Laki-laki"),
                        selectedOption = gender,
                        onOptionSelected = { gender = it }
                    )

                    InfoNote(
                        text = "Pastikan data penumpang sesuai kartu identitas untuk menghindari kendala saat keberangkatan."
                    )
                }
            }
        }

        BottomActionButton(
            text = "Simpan Penumpang",
            onClick = onSaveClick
        )
    }
}