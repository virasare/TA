package com.dicoding.tugas_akhir.core.utils

object ValidationUtils {

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun isNikValid(nik: String): Boolean {
        return nik.length == 16 && nik.all { it.isDigit() }
    }

    fun isPhoneValid(phone: String): Boolean {
        return phone.length >= 10 && phone.all { it.isDigit() }
    }

    fun isRequired(value: String): Boolean {
        return value.isNotBlank()
    }
}