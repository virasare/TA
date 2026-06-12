package com.dicoding.tugas_akhir.di

import com.dicoding.tugas_akhir.data.remote.datasource.FakeRemoteDataSource
import com.dicoding.tugas_akhir.data.repository.ScheduleRepository

object Injection {

    fun provideScheduleRepository(): ScheduleRepository {
        val remoteDataSource = FakeRemoteDataSource.getInstance()
        return ScheduleRepository.getInstance(remoteDataSource)
    }
}