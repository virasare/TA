package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.tugas_akhir.data.repository.AuthRepository
import com.dicoding.tugas_akhir.data.repository.BookingRepository
import com.dicoding.tugas_akhir.data.repository.MyTicketRepository
import com.dicoding.tugas_akhir.data.repository.PaymentRepository
import com.dicoding.tugas_akhir.data.repository.ScheduleRepository
import com.dicoding.tugas_akhir.di.Injection

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val scheduleRepository: ScheduleRepository,
    private val bookingRepository: BookingRepository,
    private val paymentRepository: PaymentRepository,
    private val myTicketRepository: MyTicketRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        }

        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(scheduleRepository) as T
        }

        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            return BookingViewModel(bookingRepository) as T
        }

        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            return PaymentViewModel(paymentRepository) as T
        }

        if (modelClass.isAssignableFrom(MyTicketViewModel::class.java)) {
            return MyTicketViewModel(myTicketRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = ViewModelFactory(
                    authRepository = Injection.provideAuthRepository(),
                    scheduleRepository = Injection.provideScheduleRepository(),
                    bookingRepository = Injection.provideBookingRepository(),
                    paymentRepository = Injection.providePaymentRepository(),
                    myTicketRepository = Injection.provideMyTicketRepository(),
                )
                INSTANCE = instance
                instance
            }
        }
    }
}