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
fun EditProfileScreen(
    initialName: String,
    initialEmail: String,
    onSaveClick: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var phone by remember { mutableStateOf("081234567890") }
    var address by remember { mutableStateOf("Denpasar, Bali") }

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
                    AvatarPreview(name = name)

                    ProfileTextField(
                        label = "Nama Lengkap",
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Masukkan nama lengkap"
                    )

                    ProfileTextField(
                        label = "Email",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Masukkan email",
                        keyboardType = KeyboardType.Email
                    )

                    ProfileTextField(
                        label = "Nomor Telepon",
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = "Masukkan nomor telepon",
                        keyboardType = KeyboardType.Phone
                    )

                    ProfileTextField(
                        label = "Alamat",
                        value = address,
                        onValueChange = { address = it },
                        placeholder = "Masukkan alamat"
                    )

                    InfoNote(
                        text = "Perubahan data profil akan digunakan untuk informasi akun dan pemesanan berikutnya."
                    )
                }
            }
        }

        BottomActionButton(
            text = "Simpan Perubahan",
            onClick = onSaveClick
        )
    }
}