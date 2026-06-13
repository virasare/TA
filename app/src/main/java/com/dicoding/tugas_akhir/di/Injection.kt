package com.dicoding.tugas_akhir.di

import com.dicoding.tugas_akhir.TugasAkhirApplication
import com.dicoding.tugas_akhir.data.local.LocalDataSource

import com.dicoding.tugas_akhir.data.local.room.AppDatabase
import com.dicoding.tugas_akhir.data.remote.datasource.FakeRemoteDataSource
import com.dicoding.tugas_akhir.data.repository.AuthRepository
import com.dicoding.tugas_akhir.data.repository.BookingRepository
import com.dicoding.tugas_akhir.data.repository.MyTicketRepository
import com.dicoding.tugas_akhir.data.repository.PaymentRepository
import com.dicoding.tugas_akhir.data.repository.ScheduleRepository
import com.google.firebase.auth.FirebaseAuth

object Injection {

    private fun provideFakeRemoteDataSource(): FakeRemoteDataSource {
        return FakeRemoteDataSource.getInstance()
    }

    private fun provideAppDatabase(): AppDatabase {
        return AppDatabase.getInstance(
            context = TugasAkhirApplication.instance,
        )
    }

    private fun provideLocalDataSource(): LocalDataSource {
        val database = provideAppDatabase()

        return LocalDataSource.getInstance(
            bookingDao = database.bookingDao(),
            paymentDao = database.paymentDao(),
        )
    }

    fun provideAuthRepository(): AuthRepository {
        return AuthRepository.getInstance(
            firebaseAuth = FirebaseAuth.getInstance(),
        )
    }

    fun provideScheduleRepository(): ScheduleRepository {
        return ScheduleRepository.getInstance(
            remoteDataSource = provideFakeRemoteDataSource(),
        )
    }

    fun provideBookingRepository(): BookingRepository {
        return BookingRepository.getInstance(
            remoteDataSource = provideFakeRemoteDataSource(),
            localDataSource = provideLocalDataSource(),
        )
    }

    fun providePaymentRepository(): PaymentRepository {
        return PaymentRepository.getInstance(
            remoteDataSource = provideFakeRemoteDataSource(),
            localDataSource = provideLocalDataSource(),
        )
    }

    fun provideMyTicketRepository(): MyTicketRepository {
        return MyTicketRepository.getInstance(
            localDataSource = provideLocalDataSource(),
        )
    }
}