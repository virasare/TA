package com.dicoding.tugas_akhir.ui.state

data class ScheduleSearchFormState(
    val origin: String = "",
    val destination: String = "",
    val date: String = "",
) {
    val isValid: Boolean
        get() = origin.isNotBlank() &&
                destination.isNotBlank() &&
                date.isNotBlank()
}