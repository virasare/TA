package com.dicoding.tugas_akhir.ui.screens.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.data.dummy.PassengerData
import com.dicoding.tugas_akhir.ui.components.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun PassengerFormScreen(
    onBackClick: () -> Unit,
    onContinueClick: (PassengerData) -> Unit
) {
    var fullName by remember {
        mutableStateOf("")
    }

    var nik by remember {
        mutableStateOf("")
    }

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var gender by remember {
        mutableStateOf("")
    }

    var savePassengerData by remember {
        mutableStateOf(true)
    }

    val isButtonEnabled =
        fullName.isNotBlank() &&
                nik.length >= 16 &&
                phoneNumber.isNotBlank() &&
                gender.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 24.dp
            )
        ) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = White,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Neutral200),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PassengerFormHeader()

                        SavedPassengerButton(
                            onClick = {
                                fullName = "Vira Sare"
                                nik = "5308123456789000"
                                phoneNumber = "081234567890"
                                gender = "Perempuan"
                            }
                        )

                        PassengerTextField(
                            label = "Nama Penumpang",
                            value = fullName,
                            onValueChange = {
                                fullName = it
                            },
                            placeholder = "Masukkan nama lengkap"
                        )

                        PassengerTextField(
                            label = "Nomor Induk Kependudukan (NIK)",
                            value = nik,
                            onValueChange = { value ->
                                if (value.length <= 16 && value.all { it.isDigit() }) {
                                    nik = value
                                }
                            },
                            placeholder = "Masukkan 16 digit NIK",
                            keyboardType = KeyboardType.Number
                        )

                        PassengerTextField(
                            label = "Nomor Telepon",
                            value = phoneNumber,
                            onValueChange = { value ->
                                if (value.all { it.isDigit() }) {
                                    phoneNumber = value
                                }
                            },
                            placeholder = "Masukkan nomor telepon",
                            keyboardType = KeyboardType.Phone
                        )

                        GenderSection(
                            selectedGender = gender,
                            onGenderSelected = {
                                gender = it
                            }
                        )

                        SavePassengerCheckBox(
                            checked = savePassengerData,
                            onCheckedChange = {
                                savePassengerData = it
                            }
                        )
                    }
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Background
        ) {
            PrimaryButton(
                text = "Lanjut",
                enabled = isButtonEnabled,
                onClick = {
                    onContinueClick(
                        PassengerData(
                            fullName = fullName,
                            nik = nik,
                            phoneNumber = phoneNumber,
                            gender = gender
                        )
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun PassengerFormHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Primary3,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Primary2,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(22.dp)
                )
            }

            Text(
                text = "Data Penumpang",
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Text(
            text = "Lengkapi data penumpang sesuai dengan kartu identitas.",
            color = Neutral500,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun SavedPassengerButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Primary3,
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Save,
                contentDescription = null,
                tint = Primary2,
                modifier = Modifier
                    .padding(8.dp)
                    .size(22.dp)
            )
        }

        Text(
            text = "Gunakan Data Tersimpan",
            color = Primary2,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
private fun PassengerTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            color = Neutral700,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodySmall
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Neutral500
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )
    }
}

@Composable
private fun GenderSection(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Jenis Kelamin",
            color = Neutral700,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodySmall
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GenderChip(
                text = "Perempuan",
                selected = selectedGender == "Perempuan",
                onClick = {
                    onGenderSelected("Perempuan")
                },
                modifier = Modifier.weight(1f)
            )

            GenderChip(
                text = "Laki-laki",
                selected = selectedGender == "Laki-laki",
                onClick = {
                    onGenderSelected("Laki-laki")
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun GenderChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) Primary3 else White
    val borderColor = if (selected) Primary2 else Neutral200
    val textColor = if (selected) Primary2 else Neutral700

    Surface(
        modifier = modifier
            .clickable {
                onClick()
            },
        color = backgroundColor,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 12.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun SavePassengerCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCheckedChange(!checked)
            },
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )

        Column(
            modifier = Modifier.padding(top = 10.dp, start = 4.dp)
        ) {
            Text(
                text = "Simpan data penumpang",
                color = Neutral700,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Data ini dapat digunakan kembali untuk pemesanan berikutnya.",
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}