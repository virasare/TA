package com.dicoding.tugas_akhir.data.remote.datasource

import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.dummy.dummyShipSchedules
import kotlinx.coroutines.delay

class FakeRemoteDataSource private constructor() {

    suspend fun getUpcomingSchedules(): List<ShipSchedule> {
        delay(900)
        return dummyShipSchedules
    }

    suspend fun getScheduleById(scheduleId: Int): ShipSchedule? {
        delay(600)
        return dummyShipSchedules.find { schedule ->
            schedule.id == scheduleId
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: FakeRemoteDataSource? = null

        fun getInstance(): FakeRemoteDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = FakeRemoteDataSource()
                INSTANCE = instance
                instance
            }
        }
    }
}