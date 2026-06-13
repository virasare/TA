package com.dicoding.tugas_akhir.di

import com.dicoding.tugas_akhir.data.remote.datasource.FakeRemoteDataSource
import com.dicoding.tugas_akhir.data.repository.BookingRepository
import com.dicoding.tugas_akhir.data.repository.PaymentRepository
import com.dicoding.tugas_akhir.data.repository.ScheduleRepository

object Injection {

    private fun provideFakeRemoteDataSource(): FakeRemoteDataSource {
        return FakeRemoteDataSource.getInstance()
    }

    fun provideScheduleRepository(): ScheduleRepository {
        return ScheduleRepository.getInstance(
            remoteDataSource = provideFakeRemoteDataSource(),
        )
    }

    fun provideBookingRepository(): BookingRepository {
        return BookingRepository.getInstance(
            remoteDataSource = provideFakeRemoteDataSource(),
        )
    }

    fun providePaymentRepository(): PaymentRepository {
        return PaymentRepository.getInstance(
            remoteDataSource = provideFakeRemoteDataSource(),
        )
    }
}