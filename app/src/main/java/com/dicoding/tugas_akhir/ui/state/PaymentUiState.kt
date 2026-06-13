package com.dicoding.tugas_akhir.ui.state

import com.dicoding.tugas_akhir.domain.model.Payment
import com.dicoding.tugas_akhir.domain.model.PaymentMethod

sealed interface PaymentMethodUiState {
    data object Loading : PaymentMethodUiState
    data class Success(val methods: List<PaymentMethod>) : PaymentMethodUiState
    data class Empty(val message: String) : PaymentMethodUiState
    data class Error(val message: String) : PaymentMethodUiState
}

sealed interface CreatePaymentUiState {
    data object Idle : CreatePaymentUiState
    data object Loading : CreatePaymentUiState
    data class Success(val payment: Payment) : CreatePaymentUiState
    data class Error(val message: String) : CreatePaymentUiState
}

sealed interface PaymentDetailUiState {
    data object Loading : PaymentDetailUiState
    data class Success(val payment: Payment) : PaymentDetailUiState
    data class Error(val message: String) : PaymentDetailUiState
}

sealed interface PaymentActionUiState {
    data object Idle : PaymentActionUiState
    data object Loading : PaymentActionUiState
    data class Success(val payment: Payment) : PaymentActionUiState
    data class Error(val message: String) : PaymentActionUiState
}