package com.dicoding.tugas_akhir.ui.components.forms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.R
import com.dicoding.tugas_akhir.ui.theme.Error
import com.dicoding.tugas_akhir.ui.theme.Neutral300
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.White

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    @DrawableRes trailingIcon: Int? = null,
    onTrailingIconClick: (() -> Unit)? = null
) {
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
            singleLine = singleLine,
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
                keyboardType = keyboardType
            ),
            trailingIcon = {
                if (trailingIcon != null) {
                    if (onTrailingIconClick != null) {
                        IconButton(
                            onClick = onTrailingIconClick
                        ) {
                            Icon(
                                painter = painterResource(id = trailingIcon),
                                contentDescription = null,
                                tint = Neutral500
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(id = trailingIcon),
                            contentDescription = null,
                            tint = Neutral500
                        )
                    }
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
    name = "AppTextField Preview",
    showBackground = true
)
@Composable
private fun AppTextFieldPreview() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var name by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("vira@gmail.com") }
                var phone by remember { mutableStateOf("") }

                AppTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nama Lengkap",
                    placeholder = "Masukkan nama lengkap",
                    trailingIcon = R.drawable.ic_person
                )

                AppTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "Masukkan email",
                    keyboardType = KeyboardType.Email,
                    trailingIcon = R.drawable.ic_message,
                )

                AppTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Nomor HP",
                    placeholder = "Contoh: 081234567890",
                    keyboardType = KeyboardType.Phone,
                    trailingIcon = R.drawable.ic_call,
                )

                AppTextField(
                    value = "",
                    onValueChange = {},
                    label = "Pelabuhan Tujuan",
                    placeholder = "Cari pelabuhan tujuan",
                    trailingIcon = R.drawable.ic_search
                )

                AppTextField(
                    value = "",
                    onValueChange = {},
                    label = "NIK",
                    placeholder = "Masukkan NIK",
                    keyboardType = KeyboardType.Number,
                    isError = true,
                    errorMessage = "NIK wajib diisi"
                )

                AppTextField(
                    value = "Ende",
                    onValueChange = {},
                    label = "Pelabuhan Asal",
                    placeholder = "Pilih pelabuhan asal",
                    enabled = false
                )
            }
        }
    }
}