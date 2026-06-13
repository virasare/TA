package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugas_akhir.core.common.Resource
import com.dicoding.tugas_akhir.data.remote.request.CreatePaymentRequest
import com.dicoding.tugas_akhir.data.repository.PaymentRepository
import com.dicoding.tugas_akhir.domain.model.PaymentMethod
import com.dicoding.tugas_akhir.ui.state.CreatePaymentUiState
import com.dicoding.tugas_akhir.ui.state.PaymentActionUiState
import com.dicoding.tugas_akhir.ui.state.PaymentDetailUiState
import com.dicoding.tugas_akhir.ui.state.PaymentMethodUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val paymentRepository: PaymentRepository,
) : ViewModel() {

    private val _paymentMethodUiState = MutableStateFlow<PaymentMethodUiState>(
        PaymentMethodUiState.Loading
    )
    val paymentMethodUiState: StateFlow<PaymentMethodUiState> =
        _paymentMethodUiState.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow<PaymentMethod?>(null)
    val selectedPaymentMethod: StateFlow<PaymentMethod?> =
        _selectedPaymentMethod.asStateFlow()

    private val _createPaymentUiState = MutableStateFlow<CreatePaymentUiState>(
        CreatePaymentUiState.Idle
    )
    val createPaymentUiState: StateFlow<CreatePaymentUiState> =
        _createPaymentUiState.asStateFlow()

    private val _paymentDetailUiState = MutableStateFlow<PaymentDetailUiState>(
        PaymentDetailUiState.Loading
    )
    val paymentDetailUiState: StateFlow<PaymentDetailUiState> =
        _paymentDetailUiState.asStateFlow()

    private val _paymentActionUiState = MutableStateFlow<PaymentActionUiState>(
        PaymentActionUiState.Idle
    )
    val paymentActionUiState: StateFlow<PaymentActionUiState> =
        _paymentActionUiState.asStateFlow()

    fun loadPaymentMethods() {
        viewModelScope.launch {
            paymentRepository.getPaymentMethods().collect { result ->
                _paymentMethodUiState.value = when (result) {
                    is Resource.Loading -> PaymentMethodUiState.Loading
                    is Resource.Success -> PaymentMethodUiState.Success(result.data)
                    is Resource.Empty -> PaymentMethodUiState.Empty("Metode pembayaran belum tersedia")
                    is Resource.Error -> PaymentMethodUiState.Error(result.message)
                }
            }
        }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
    }

    fun createPayment(bookingId: String) {
        val selectedMethod = _selectedPaymentMethod.value

        if (selectedMethod == null) {
            _createPaymentUiState.value = CreatePaymentUiState.Error(
                "Pilih metode pembayaran terlebih dahulu"
            )
            return
        }

        val request = CreatePaymentRequest(
            bookingId = bookingId,
            paymentMethodId = selectedMethod.id,
        )

        viewModelScope.launch {
            paymentRepository.createPayment(request).collect { result ->
                _createPaymentUiState.value = when (result) {
                    is Resource.Loading -> CreatePaymentUiState.Loading
                    is Resource.Success -> CreatePaymentUiState.Success(result.data)
                    is Resource.Empty -> CreatePaymentUiState.Error("Pembayaran gagal dibuat")
                    is Resource.Error -> CreatePaymentUiState.Error(result.message)
                }
            }
        }
    }

    fun getPaymentDetail(paymentId: String) {
        viewModelScope.launch {
            paymentRepository.getPaymentDetail(paymentId).collect { result ->
                _paymentDetailUiState.value = when (result) {
                    is Resource.Loading -> PaymentDetailUiState.Loading
                    is Resource.Success -> PaymentDetailUiState.Success(result.data)
                    is Resource.Empty -> PaymentDetailUiState.Error("Pembayaran tidak ditemukan")
                    is Resource.Error -> PaymentDetailUiState.Error(result.message)
                }
            }
        }
    }

    fun simulatePaymentSuccess(paymentId: String) {
        viewModelScope.launch {
            paymentRepository.simulatePaymentSuccess(paymentId).collect { result ->
                _paymentActionUiState.value = when (result) {
                    is Resource.Loading -> PaymentActionUiState.Loading
                    is Resource.Success -> PaymentActionUiState.Success(result.data)
                    is Resource.Empty -> PaymentActionUiState.Error("Status pembayaran tidak ditemukan")
                    is Resource.Error -> PaymentActionUiState.Error(result.message)
                }
            }
        }
    }

    fun resetCreatePaymentState() {
        _createPaymentUiState.value = CreatePaymentUiState.Idle
    }

    fun resetPaymentActionState() {
        _paymentActionUiState.value = PaymentActionUiState.Idle
    }
}