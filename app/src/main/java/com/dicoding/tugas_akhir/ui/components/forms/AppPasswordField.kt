package com.dicoding.tugas_akhir.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.theme.Error
import com.dicoding.tugas_akhir.ui.theme.Neutral300
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun AppPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Neutral700
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            isError = isError,
            singleLine = true,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Neutral500
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Neutral700
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Outlined.Visibility
                        } else {
                            Icons.Outlined.VisibilityOff
                        },
                        contentDescription = if (passwordVisible) {
                            "Sembunyikan password"
                        } else {
                            "Tampilkan password"
                        },
                        tint = Neutral500
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                disabledContainerColor = White,
                errorContainerColor = White,

                focusedBorderColor = Primary2,
                unfocusedBorderColor = Neutral300,
                disabledBorderColor = Neutral300,
                errorBorderColor = Error,

                cursorColor = Primary2,
                errorCursorColor = Error,

                focusedTextColor = Neutral700,
                unfocusedTextColor = Neutral700,
                disabledTextColor = Neutral500,
                errorTextColor = Neutral700
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        if (isError && !errorMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Error,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

@Preview(
    name = "Password Field Preview",
    showBackground = true,
    widthDp = 360
)
@Composable
fun AppPasswordFieldPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppPasswordField(
            value = "",
            onValueChange = {},
            label = "Password",
            placeholder = "Masukkan password"
        )

        AppPasswordField(
            value = "password123",
            onValueChange = {},
            label = "Password",
            placeholder = "Masukkan password"
        )

        AppPasswordField(
            value = "123",
            onValueChange = {},
            label = "Password",
            placeholder = "Masukkan password",
            isError = true,
            errorMessage = "Password minimal 8 karakter"
        )

        AppPasswordField(
            value = "password123",
            onValueChange = {},
            label = "Password",
            placeholder = "Masukkan password",
            enabled = false
        )
    }
}