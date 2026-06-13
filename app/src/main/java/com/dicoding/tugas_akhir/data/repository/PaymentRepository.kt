package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.core.common.Resource
import com.dicoding.tugas_akhir.data.mapper.DataMapper
import com.dicoding.tugas_akhir.data.remote.datasource.FakeRemoteDataSource
import com.dicoding.tugas_akhir.data.remote.request.CreatePaymentRequest
import com.dicoding.tugas_akhir.domain.model.Payment
import com.dicoding.tugas_akhir.domain.model.PaymentMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class PaymentRepository private constructor(
    private val remoteDataSource: FakeRemoteDataSource,
) {

    fun getPaymentMethods(): Flow<Resource<List<PaymentMethod>>> = flow {
        emit(Resource.Loading)

        val response = remoteDataSource.getPaymentMethods()

        if (response.isEmpty()) {
            emit(Resource.Empty)
        } else {
            emit(Resource.Success(DataMapper.mapPaymentMethodResponsesToDomain(response)))
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil metode pembayaran"))
    }

    fun createPayment(
        request: CreatePaymentRequest,
    ): Flow<Resource<Payment>> = flow {
        emit(Resource.Loading)

        val response = remoteDataSource.createPayment(request)
        val payment = DataMapper.mapPaymentResponseToDomain(response)

        emit(Resource.Success(payment))
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal membuat pembayaran"))
    }

    fun getPaymentDetail(
        paymentId: String,
    ): Flow<Resource<Payment>> = flow {
        emit(Resource.Loading)

        val response = remoteDataSource.getPaymentDetail(paymentId)

        if (response == null) {
            emit(Resource.Error("Data pembayaran tidak ditemukan"))
        } else {
            emit(Resource.Success(DataMapper.mapPaymentResponseToDomain(response)))
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengambil detail pembayaran"))
    }

    fun simulatePaymentSuccess(
        paymentId: String,
    ): Flow<Resource<Payment>> = flow {
        emit(Resource.Loading)

        val response = remoteDataSource.simulatePaymentSuccess(paymentId)
        val payment = DataMapper.mapPaymentResponseToDomain(response)

        emit(Resource.Success(payment))
    }.catch { exception ->
        emit(Resource.Error(exception.message ?: "Gagal mengecek status pembayaran"))
    }

    companion object {
        @Volatile
        private var INSTANCE: PaymentRepository? = null

        fun getInstance(
            remoteDataSource: FakeRemoteDataSource,
        ): PaymentRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = PaymentRepository(remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }
}