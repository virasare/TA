package com.dicoding.tugas_akhir.ui.state

import com.dicoding.tugas_akhir.data.dummy.ShipSchedule

sealed interface ScheduleUiState {
    data object Loading : ScheduleUiState
    data class Success(val schedules: List<ShipSchedule>) : ScheduleUiState
    data class Empty(val message: String) : ScheduleUiState
    data class Error(val message: String) : ScheduleUiState
}

sealed interface ScheduleDetailUiState {
    data object Loading : ScheduleDetailUiState
    data class Success(val schedule: ShipSchedule) : ScheduleDetailUiState
    data class Error(val message: String) : ScheduleDetailUiState
}