package com.dicoding.tugas_akhir.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dicoding.tugas_akhir.R
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Error
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun LoginScreen(
    onLoginClick: (String, String, (String) -> Unit) -> Unit,
    onGoogleLoginClick: ((String) -> Unit) -> Unit,
    onRegisterClick: () -> Unit,
    onContinueAsGuestClick: () -> Unit,
) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val isButtonEnabled = email.isNotBlank() && password.isNotBlank() && !isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .navigationBarsPadding()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.size(48.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
            )

            Text(
                text = "Masuk ke Akun",
                color = Neutral700,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 24.dp),
            )

            Text(
                text = "Masuk untuk memesan tiket dan melihat riwayat perjalanan",
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 8.dp, bottom = 28.dp),
            )

            LoginTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = ""
                },
                label = "Email",
                placeholder = "contoh@email.com",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_message_outlined),
                        contentDescription = null,
                        tint = Primary2,
                    )
                },
                keyboardType = KeyboardType.Email,
                enabled = !isLoading,
            )

            LoginTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = ""
                },
                label = "Password",
                placeholder = "Masukkan password",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock_outlined),
                        contentDescription = null,
                        tint = Primary2,
                    )
                },
                trailingIcon = {
                    IconButton(
                        enabled = !isLoading,
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) {
                                    R.drawable.ic_show_outlined
                                } else {
                                    R.drawable.ic_hide_outlined
                                }
                            ),
                            contentDescription = null,
                            tint = Neutral500,
                        )
                    }
                },
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                enabled = !isLoading,
                modifier = Modifier.padding(top = 12.dp),
            )

            if (errorMessage.isNotBlank()) {
                ErrorMessageBox(
                    message = errorMessage,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }

            PrimaryButton(
                text = if (isLoading) "Memproses..." else "Masuk",
                enabled = isButtonEnabled,
                onClick = {
                    isLoading = true
                    errorMessage = ""

                    onLoginClick(email, password) { message ->
                        isLoading = false
                        errorMessage = message
                    }
                },
                modifier = Modifier.padding(top = 18.dp),
            )

            Text(
                text = "atau masuk dengan",
                color = Neutral500,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 28.dp, bottom = 16.dp),
            )

            GoogleLoginButton(
                enabled = !isLoading,
                onClick = {
                    isLoading = true
                    errorMessage = ""

                    onGoogleLoginClick { message ->
                        isLoading = false
                        errorMessage = message
                    }
                },
                modifier = Modifier.padding(top = 12.dp),
            )

            Row(
                modifier = Modifier.padding(top = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Belum punya akun?",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodySmall,
                )

                Text(
                    text = " Daftar",
                    color = Primary2,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.clickable(
                        enabled = !isLoading,
                        onClick = onRegisterClick,
                    ),
                )
            }

            Spacer(modifier = Modifier.size(48.dp))

            GuestAccessCard(
                enabled = !isLoading,
                onClick = onContinueAsGuestClick,
            )
        }
        if (isLoading) {
            LoginLoadingOverlay()
        }
    }
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        label = {
            Text(label)
        },
        placeholder = {
            Text(placeholder)
        },
        singleLine = true,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary2,
            unfocusedBorderColor = Color(0xFFE3EAF2),
            focusedLabelColor = Primary2,
            cursorColor = Primary2,
            focusedContainerColor = White,
            unfocusedContainerColor = White,
            disabledContainerColor = White,
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
private fun GoogleLoginButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = onClick,
            ),
        shape = RoundedCornerShape(12.dp),
        color = White,
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE3EAF2),
        ),
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = "Masuk dengan Google",
                color = Neutral700,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun ErrorMessageBox(
    message: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFFFEBEE),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFFFCDD2),
        ),
    ) {
        Text(
            text = message,
            color = Error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(12.dp),
        )
    }
}

@Composable
private fun LoginLoadingOverlay(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable(
                indication = null,
                interactionSource = remember {
                    androidx.compose.foundation.interaction.MutableInteractionSource()
                },
                onClick = {}
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            LottieAnimation(
                composition = composition,
                progress = {
                    progress
                },
                modifier = Modifier.size(160.dp),
            )

            Text(
                text = "Memproses Login",
                color = White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun GuestAccessCard(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = onClick,
            ),
        shape = RoundedCornerShape(20.dp),
        color = Primary3,
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFD7EAFE),
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                color = White,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DirectionsBoat,
                        contentDescription = null,
                        tint = Primary2,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = "Lihat Jadwal Tanpa Login",
                    color = Neutral700,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = "Kamu tetap bisa mencari jadwal kapal terlebih dahulu.",
                    color = Neutral500,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Text(
                text = "→",
                color = Primary2,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        onLoginClick = { _, _, _ -> },
        onGoogleLoginClick = { _ -> },
        onRegisterClick = {},
        onContinueAsGuestClick = {},
    )
}