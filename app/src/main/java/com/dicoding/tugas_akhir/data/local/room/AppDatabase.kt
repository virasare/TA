package com.dicoding.tugas_akhir.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.tugas_akhir.data.local.room.dao.BookingDao
import com.dicoding.tugas_akhir.data.local.room.dao.NotificationDao
import com.dicoding.tugas_akhir.data.local.room.dao.PaymentDao
import com.dicoding.tugas_akhir.data.local.room.entity.BookingEntity
import com.dicoding.tugas_akhir.data.local.room.entity.NotificationEntity
import com.dicoding.tugas_akhir.data.local.room.entity.PassengerEntity
import com.dicoding.tugas_akhir.data.local.room.entity.PaymentEntity
import com.dicoding.tugas_akhir.data.local.room.dao.SavedPassengerDao
import com.dicoding.tugas_akhir.data.local.room.entity.SavedPassengerEntity

@Database(
    entities = [
        BookingEntity::class,
        PassengerEntity::class,
        PaymentEntity::class,
        NotificationEntity::class,
        SavedPassengerEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookingDao(): BookingDao

    abstract fun paymentDao(): PaymentDao

    abstract fun notificationDao(): NotificationDao

    abstract fun savedPassengerDao(): SavedPassengerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(
            context: Context,
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tugas_akhir_database",
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}