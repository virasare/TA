package com.dicoding.tugas_akhir.core.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object ImagePickerUtils {

    fun createImageUri(context: Context): Uri {
        val imageFolder = File(
            context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES),
            "profile"
        )

        if (!imageFolder.exists()) {
            imageFolder.mkdirs()
        }

        val imageFile = File(
            imageFolder,
            "profile_${System.currentTimeMillis()}.jpg"
        )

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    }
}