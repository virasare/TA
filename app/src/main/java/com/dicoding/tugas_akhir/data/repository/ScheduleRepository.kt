package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.core.common.Resource
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.remote.datasource.FakeRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ScheduleRepository private constructor(
    private val remoteDataSource: FakeRemoteDataSource
) {

    fun getUpcomingSchedules(): Flow<Resource<List<ShipSchedule>>> = flow {
        emit(Resource.Loading)

        val schedules = remoteDataSource.getUpcomingSchedules()

        if (schedules.isEmpty()) {
            emit(Resource.Empty)
        } else {
            emit(Resource.Success(schedules))
        }
    }.catch { exception ->
        emit(
            Resource.Error(
                exception.message ?: "Terjadi kesalahan saat mengambil jadwal kapal"
            )
        )
    }

    fun getScheduleDetail(scheduleId: String): Flow<Resource<ShipSchedule>> = flow {
        emit(Resource.Loading)

        val schedule = remoteDataSource.getScheduleById(scheduleId)

        if (schedule == null) {
            emit(Resource.Error("Detail jadwal tidak ditemukan"))
        } else {
            emit(Resource.Success(schedule))
        }
    }.catch { exception ->
        emit(
            Resource.Error(
                exception.message ?: "Terjadi kesalahan saat mengambil detail jadwal"
            )
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: ScheduleRepository? = null

        fun getInstance(
            remoteDataSource: FakeRemoteDataSource
        ): ScheduleRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = ScheduleRepository(remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }
}