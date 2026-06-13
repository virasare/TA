package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.tugas_akhir.di.Injection

class ViewModelFactory private constructor() : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(
                authRepository = Injection.provideAuthRepository()
            ) as T
        }

        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(
                scheduleRepository = Injection.provideScheduleRepository()
            ) as T
        }

        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            return BookingViewModel(
                bookingRepository = Injection.provideBookingRepository()
            ) as T
        }

        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            return PaymentViewModel(
                paymentRepository = Injection.providePaymentRepository()
            ) as T
        }

        if (modelClass.isAssignableFrom(MyTicketViewModel::class.java)) {
            return MyTicketViewModel(
                myTicketRepository = Injection.provideMyTicketRepository()
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = ViewModelFactory()
                INSTANCE = instance
                instance
            }
        }
    }
}