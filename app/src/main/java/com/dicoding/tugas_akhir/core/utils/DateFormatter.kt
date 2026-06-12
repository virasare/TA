package com.dicoding.tugas_akhir.core.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {

    fun formatDate(input: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            val date = inputFormat.parse(input)
            if (date != null) outputFormat.format(date) else input
        } catch (e: Exception) {
            input
        }
    }
}