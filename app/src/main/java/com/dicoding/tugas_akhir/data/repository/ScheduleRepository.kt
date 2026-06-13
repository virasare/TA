package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.core.common.Resource
import com.dicoding.tugas_akhir.data.mapper.DataMapper
import com.dicoding.tugas_akhir.data.remote.datasource.FakeRemoteDataSource
import com.dicoding.tugas_akhir.domain.model.ShipSchedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ScheduleRepository private constructor(
    private val remoteDataSource: FakeRemoteDataSource,
) {

    fun getUpcomingSchedules(): Flow<Resource<List<ShipSchedule>>> =
        flow<Resource<List<ShipSchedule>>> {
            emit(Resource.Loading)

            val response = remoteDataSource.getUpcomingSchedules()
            val schedules = DataMapper.mapScheduleResponsesToDomain(response)

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

    fun searchSchedules(
        origin: String,
        destination: String,
        date: String,
    ): Flow<Resource<List<ShipSchedule>>> =
        flow<Resource<List<ShipSchedule>>> {
            emit(Resource.Loading)

            val response = remoteDataSource.searchSchedules(
                origin = origin,
                destination = destination,
                date = date,
            )

            val schedules = DataMapper.mapScheduleResponsesToDomain(response)

            if (schedules.isEmpty()) {
                emit(Resource.Empty)
            } else {
                emit(Resource.Success(schedules))
            }
        }.catch { exception ->
            emit(
                Resource.Error(
                    exception.message ?: "Terjadi kesalahan saat mencari jadwal kapal"
                )
            )
        }

    fun getScheduleDetail(
        scheduleId: String,
    ): Flow<Resource<ShipSchedule>> =
        flow<Resource<ShipSchedule>> {
            emit(Resource.Loading)

            val response = remoteDataSource.getScheduleDetail(scheduleId)

            if (response == null) {
                emit(Resource.Error("Detail jadwal tidak ditemukan"))
            } else {
                emit(Resource.Success(DataMapper.mapScheduleResponseToDomain(response)))
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
            remoteDataSource: FakeRemoteDataSource,
        ): ScheduleRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = ScheduleRepository(remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }
}