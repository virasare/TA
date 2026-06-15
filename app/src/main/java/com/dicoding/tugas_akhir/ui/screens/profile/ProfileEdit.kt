package com.dicoding.tugas_akhir.ui.screens.profile

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.tugas_akhir.core.utils.ImagePickerUtils
import com.dicoding.tugas_akhir.ui.components.profile.AvatarPreview
import com.dicoding.tugas_akhir.ui.components.profile.BottomActionButton
import com.dicoding.tugas_akhir.ui.components.profile.InfoNote
import com.dicoding.tugas_akhir.ui.components.profile.ProfileFormCard
import com.dicoding.tugas_akhir.ui.components.profile.ProfileTextField
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.viewmodel.ProfileViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    initialName: String = "",
    initialEmail: String = "",
    initialPhotoUrl: String = "",
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    ),
) {
    val context = LocalContext.current
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()

    LaunchedEffect(initialName, initialEmail, initialPhotoUrl) {
        viewModel.setInitialProfileIfEmpty(
            name = initialName,
            email = initialEmail,
            photoUri = initialPhotoUrl,
        )
    }

    var showPhotoSheet by remember { mutableStateOf(false) }
    var showSavedDialog by remember { mutableStateOf(false) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION,
            )
            viewModel.updatePhotoUri(uri.toString())
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        if (success) {
            cameraUri?.let { uri ->
                viewModel.updatePhotoUri(uri.toString())
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            val uri = ImagePickerUtils.createImageUri(context)
            cameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    LaunchedEffect(isSaved) {
        if (isSaved) {
            showSavedDialog = true
            viewModel.resetSavedState()
        }
    }

    if (showPhotoSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showPhotoSheet = false
            },
            containerColor = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Ubah Foto Profil",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF102A43),
                )

                Text(
                    text = "Pilih sumber foto yang ingin digunakan.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF627D98),
                )

                PhotoSourceCard(
                    title = "Pilih dari Galeri",
                    subtitle = "Ambil foto dari penyimpanan perangkat.",
                    icon = Icons.Outlined.PhotoLibrary,
                    onClick = {
                        showPhotoSheet = false
                        galleryLauncher.launch(arrayOf("image/*"))
                    },
                )

                PhotoSourceCard(
                    title = "Ambil dari Kamera",
                    subtitle = "Gunakan kamera untuk foto baru.",
                    icon = Icons.Outlined.PhotoCamera,
                    onClick = {
                        showPhotoSheet = false
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                )
            }
        }
    }

    if (showSavedDialog) {
        AlertDialog(
            onDismissRequest = {
                showSavedDialog = false
            },
            title = {
                Text("Profil Berhasil Disimpan")
            },
            text = {
                Text("Perubahan nama, foto, dan data profil berhasil disimpan.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSavedDialog = false
                        onSaveClick()
                    },
                ) {
                    Text("Oke")
                }
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAFC)),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                ProfileFormCard(
                    title = "Foto Profil",
                ) {
                    AvatarPreview(
                        name = profile.name.ifBlank { initialName },
                        email = profile.email.ifBlank { initialEmail },
                        photoUrl = profile.photoUri.ifBlank { null },
                        onChangePhotoClick = {
                            showPhotoSheet = true
                        },
                    )
                }
            }

            item {
                ProfileFormCard(
                    title = "Informasi Akun",
                ) {
                    ProfileTextField(
                        label = "Nama Lengkap",
                        value = profile.name,
                        onValueChange = viewModel::updateName,
                        placeholder = "Masukkan nama lengkap",
                    )

                    ProfileTextField(
                        label = "Email",
                        value = profile.email,
                        onValueChange = viewModel::updateEmail,
                        placeholder = "Masukkan email",
                        keyboardType = KeyboardType.Email,
                    )

                    ProfileTextField(
                        label = "Nomor Telepon",
                        value = profile.phoneNumber,
                        onValueChange = viewModel::updatePhone,
                        placeholder = "Masukkan nomor telepon",
                        keyboardType = KeyboardType.Phone,
                    )

                    ProfileTextField(
                        label = "Alamat",
                        value = profile.address,
                        onValueChange = viewModel::updateAddress,
                        placeholder = "Masukkan alamat",
                        singleLine = false,
                    )

                    InfoNote(
                        title = "Informasi",
                        text = "Data profil yang disimpan akan digunakan sebagai informasi akun dan dapat membantu proses pemesanan berikutnya.",
                    )
                }
            }
        }

        BottomActionButton(
            text = "Simpan Perubahan",
            onClick = {
                viewModel.saveProfile()
            },
            enabled = profile.name.isNotBlank() && profile.email.isNotBlank(),
            modifier = Modifier.navigationBarsPadding(),
        )
    }
}

@Composable
private fun PhotoSourceCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEAF4FF),
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Primary2,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF102A43),
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF627D98),
                )
            }
        }
    }
}