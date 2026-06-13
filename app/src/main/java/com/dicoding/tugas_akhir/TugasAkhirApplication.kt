package com.dicoding.tugas_akhir

import android.app.Application

class TugasAkhirApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: TugasAkhirApplication
            private set
    }
}