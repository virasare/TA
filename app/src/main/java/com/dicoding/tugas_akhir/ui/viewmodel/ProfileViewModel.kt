package com.dicoding.tugas_akhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugas_akhir.data.repository.ProfileRepository
import com.dicoding.tugas_akhir.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            profileRepository.getProfile().collect { profile ->
                _profile.value = profile
            }
        }
    }

    fun updateName(value: String) {
        _profile.value = _profile.value.copy(name = value)
        _isSaved.value = false
    }

    fun updateEmail(value: String) {
        _profile.value = _profile.value.copy(email = value)
        _isSaved.value = false
    }

    fun updatePhone(value: String) {
        _profile.value = _profile.value.copy(
            phoneNumber = value.filter { it.isDigit() }
        )
        _isSaved.value = false
    }

    fun updateAddress(value: String) {
        _profile.value = _profile.value.copy(address = value)
        _isSaved.value = false
    }

    fun updatePhotoUri(value: String) {
        _profile.value = _profile.value.copy(photoUri = value)
        _isSaved.value = false
    }

    fun saveProfile() {
        viewModelScope.launch {
            profileRepository.saveProfile(_profile.value)
            _isSaved.value = true
        }
    }

    fun resetSavedState() {
        _isSaved.value = false
    }
}