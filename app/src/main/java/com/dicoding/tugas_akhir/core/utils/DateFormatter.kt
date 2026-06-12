package com.dicoding.tugas_akhir.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatter {

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(input: String): String {
        return try {
            val date = LocalDate.parse(input)
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
            date.format(formatter)
        } catch (e: Exception) {
            input
        }
    }
}