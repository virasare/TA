package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.tugas_akhir.data.repository.AuthRepository
import com.dicoding.tugas_akhir.data.repository.BookingRepository
import com.dicoding.tugas_akhir.data.repository.MyTicketRepository
import com.dicoding.tugas_akhir.data.repository.NotificationRepository
import com.dicoding.tugas_akhir.data.repository.PaymentRepository
import com.dicoding.tugas_akhir.data.repository.ProfileRepository
import com.dicoding.tugas_akhir.data.repository.SavedPassengerRepository
import com.dicoding.tugas_akhir.data.repository.ScheduleRepository
import com.dicoding.tugas_akhir.data.repository.SettingsRepository
import com.dicoding.tugas_akhir.di.Injection

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val scheduleRepository: ScheduleRepository,
    private val bookingRepository: BookingRepository,
    private val paymentRepository: PaymentRepository,
    private val myTicketRepository: MyTicketRepository,
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val savedPassengerRepository: SavedPassengerRepository,
    private val notificationRepository: NotificationRepository,
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

        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingsRepository) as T
        }

        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(profileRepository) as T
        }

        if (modelClass.isAssignableFrom(SavedPassengerViewModel::class.java)) {
            return SavedPassengerViewModel(savedPassengerRepository) as T
        }

        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(notificationRepository) as T
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
                    settingsRepository = Injection.provideSettingsRepository(),
                    profileRepository = Injection.provideProfileRepository(),
                    savedPassengerRepository = Injection.provideSavedPassengerRepository(),
                    notificationRepository = Injection.provideNotificationRepository(),
                )
                INSTANCE = instance
                instance
            }
        }
    }
}