package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.core.common.Resource
import com.dicoding.tugas_akhir.data.mapper.DataMapper
import com.dicoding.tugas_akhir.data.remote.datasource.FakeRemoteDataSource
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.domain.model.ETicket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MyTicketRepository private constructor(
    private val remoteDataSource: FakeRemoteDataSource,
) {

    fun getMyTickets(): Flow<Resource<List<Booking>>> = flow {
        emit(Resource.Loading)

        val response = remoteDataSource.getMyTickets()
        val tickets = response.map { DataMapper.mapBookingResponseToDomain(it) }

        if (tickets.isEmpty()) {
            emit(Resource.Empty)
        } else {
            emit(Resource.Success(tickets))
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil daftar tiket"))
    }

    fun getETicketByBookingId(
        bookingId: String,
    ): Flow<Resource<ETicket>> = flow {
        emit(Resource.Loading)

        val response = remoteDataSource.getETicketByBookingId(bookingId)

        if (response == null) {
            emit(Resource.Error("E-ticket tidak ditemukan"))
        } else {
            emit(Resource.Success(DataMapper.mapETicketResponseToDomain(response)))
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil e-ticket"))
    }

    fun getETicketByPaymentId(
        paymentId: String,
    ): Flow<Resource<ETicket>> = flow {
        emit(Resource.Loading)

        val response = remoteDataSource.getETicketByPaymentId(paymentId)

        if (response == null) {
            emit(Resource.Error("E-ticket tidak ditemukan"))
        } else {
            emit(Resource.Success(DataMapper.mapETicketResponseToDomain(response)))
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil e-ticket"))
    }

    companion object {
        @Volatile
        private var INSTANCE: MyTicketRepository? = null

        fun getInstance(
            remoteDataSource: FakeRemoteDataSource,
        ): MyTicketRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = MyTicketRepository(remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }
}