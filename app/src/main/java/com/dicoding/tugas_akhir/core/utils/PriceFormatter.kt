package com.dicoding.tugas_akhir.core.utils

import java.text.NumberFormat
import java.util.Locale

object PriceFormatter {

    fun formatToRupiah(price: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return formatter.format(price)
            .replace(",00", "")
            .replace("Rp", "Rp ")
    }
}