package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.core.common.Resource
import com.dicoding.tugas_akhir.data.local.LocalDataSource
import com.dicoding.tugas_akhir.data.mapper.DataMapper
import com.dicoding.tugas_akhir.data.remote.datasource.FakeRemoteDataSource
import com.dicoding.tugas_akhir.data.remote.request.CreateBookingRequest
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.domain.model.TicketClassOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class BookingRepository private constructor(
    private val remoteDataSource: FakeRemoteDataSource,
    private val localDataSource: LocalDataSource,
) {

    fun getTicketClassOptions(
        scheduleId: String,
    ): Flow<Resource<List<TicketClassOption>>> = flow {
        emit(Resource.Loading)

        val options = remoteDataSource.getTicketClassOptions(scheduleId)

        if (options.isEmpty()) {
            emit(Resource.Empty)
        } else {
            emit(Resource.Success(options))
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil kelas tiket"))
    }

    fun createBooking(
        request: CreateBookingRequest,
    ): Flow<Resource<Booking>> = flow {
        emit(Resource.Loading)

        val response = remoteDataSource.createBooking(request)
        val booking = DataMapper.mapBookingResponseToDomain(response)

        localDataSource.saveBooking(booking)

        emit(Resource.Success(booking))
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal membuat pesanan"))
    }

    fun getBookingDetail(
        bookingId: String,
    ): Flow<Resource<Booking>> = flow {
        emit(Resource.Loading)

        val localBooking = localDataSource.getBookingById(bookingId)

        if (localBooking != null) {
            emit(Resource.Success(DataMapper.mapBookingWithPassengersToDomain(localBooking)))
            return@flow
        }

        val response = remoteDataSource.getBookingDetail(bookingId)

        if (response == null) {
            emit(Resource.Error("Pesanan tidak ditemukan"))
        } else {
            val booking = DataMapper.mapBookingResponseToDomain(response)
            localDataSource.saveBooking(booking)
            emit(Resource.Success(booking))
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil detail pesanan"))
    }

    companion object {
        @Volatile
        private var INSTANCE: BookingRepository? = null

        fun getInstance(
            remoteDataSource: FakeRemoteDataSource,
            localDataSource: LocalDataSource,
        ): BookingRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = BookingRepository(
                    remoteDataSource = remoteDataSource,
                    localDataSource = localDataSource,
                )
                INSTANCE = instance
                instance
            }
        }
    }
}