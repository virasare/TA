package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugas_akhir.core.common.Resource
import com.dicoding.tugas_akhir.data.repository.ScheduleRepository
import com.dicoding.tugas_akhir.ui.state.ScheduleDetailUiState
import com.dicoding.tugas_akhir.ui.state.ScheduleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _scheduleUiState = MutableStateFlow<ScheduleUiState>(
        ScheduleUiState.Loading
    )
    val scheduleUiState: StateFlow<ScheduleUiState> =
        _scheduleUiState.asStateFlow()

    private val _scheduleDetailUiState = MutableStateFlow<ScheduleDetailUiState>(
        ScheduleDetailUiState.Loading
    )
    val scheduleDetailUiState: StateFlow<ScheduleDetailUiState> =
        _scheduleDetailUiState.asStateFlow()

    fun getUpcomingSchedules() {
        viewModelScope.launch {
            scheduleRepository.getUpcomingSchedules().collect { result ->
                _scheduleUiState.value = when (result) {
                    is Resource.Loading -> {
                        ScheduleUiState.Loading
                    }

                    is Resource.Success -> {
                        ScheduleUiState.Success(result.data)
                    }

                    is Resource.Empty -> {
                        ScheduleUiState.Empty("Belum ada jadwal kapal tersedia.")
                    }

                    is Resource.Error -> {
                        ScheduleUiState.Error(result.message)
                    }
                }
            }
        }
    }

    fun getScheduleDetail(scheduleId: Int) {
        viewModelScope.launch {
            scheduleRepository.getScheduleDetail(scheduleId).collect { result ->
                _scheduleDetailUiState.value = when (result) {
                    is Resource.Loading -> {
                        ScheduleDetailUiState.Loading
                    }

                    is Resource.Success -> {
                        ScheduleDetailUiState.Success(result.data)
                    }

                    is Resource.Empty -> {
                        ScheduleDetailUiState.Error("Detail jadwal tidak ditemukan")
                    }

                    is Resource.Error -> {
                        ScheduleDetailUiState.Error(result.message)
                    }
                }
            }
        }
    }
}