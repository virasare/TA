package com.dicoding.tugas_akhir.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun ProfileScreen(
    name: String,
    email: String,
    onEditProfileClick: () -> Unit,
    onPassengerDataClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding(),
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            bottom = 100.dp
        )
    ) {
        item {
            ProfileHeader(
                name = name,
                email = email
            )
        }

        item {
            ProfileSectionTitle(text = "Akun")
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                shape = RoundedCornerShape(14.dp)
            ) {
                Column {
                    ProfileMenuItem(
                        icon = Icons.Outlined.Edit,
                        title = "Edit Profil",
                        description = "Ubah nama dan informasi akun",
                        onClick = onEditProfileClick
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.Person,
                        title = "Data Penumpang",
                        description = "Kelola data penumpang tersimpan",
                        onClick = onPassengerDataClick
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.Settings,
                        title = "Pengaturan",
                        description = "Bahasa, tema, dan preferensi aplikasi",
                        onClick = onSettingsClick
                    )
                }
            }
        }

        item {
            ProfileSectionTitle(
                text = "Layanan",
                modifier = Modifier.padding(top = 24.dp)
            )
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                shape = RoundedCornerShape(14.dp)
            ) {
                Column {
                    ProfileMenuItem(
                        icon = Icons.Outlined.Help,
                        title = "Bantuan",
                        description = "Panduan dan pusat bantuan",
                        onClick = onHelpClick
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.Info,
                        title = "Tentang Aplikasi",
                        description = "Informasi aplikasi",
                        onClick = onAboutClick
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.Logout,
                        title = "Keluar Akun",
                        description = "Keluar dari akun saat ini",
                        onClick = onLogoutClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    name: String,
    email: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp, bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InitialAvatar(
            name = name,
            size = 86
        )

        Text(
            text = name,
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 14.dp)
        )

        Text(
            text = email,
            color = Neutral700,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun InitialAvatar(
    name: String,
    size: Int
) {
    val initials = name.toInitials()

    Surface(
        modifier = Modifier.size(size.dp),
        shape = CircleShape,
        color = Color(0xFFFFEAF3),
        border = BorderStroke(3.dp, Color(0xFFFFB3D1))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = initials,
                color = Color(0xFFC2185B),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
private fun ProfileSectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Neutral700,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Primary3,
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Primary2,
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                color = Neutral700,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = description,
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Neutral500,
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun String.toInitials(): String {
    val words = trim()
        .split(" ")
        .filter { it.isNotBlank() }

    return when {
        words.size >= 2 -> {
            "${words[0].first()}${words[1].first()}".uppercase()
        }

        words.size == 1 && words[0].length >= 2 -> {
            words[0].take(2).uppercase()
        }

        words.size == 1 -> {
            words[0].take(1).uppercase()
        }

        else -> {
            "U"
        }
    }
}