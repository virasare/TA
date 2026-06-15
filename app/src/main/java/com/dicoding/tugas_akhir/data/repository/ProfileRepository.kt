package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.data.local.datastore.ProfileDataStore
import com.dicoding.tugas_akhir.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ProfileRepository private constructor(
    private val profileDataStore: ProfileDataStore,
    private val authRepository: AuthRepository,
) {

    fun getProfile(): Flow<UserProfile> {
        val firebaseUser = authRepository.getCurrentUser()

        if (firebaseUser == null) {
            return flowOf(UserProfile())
        }

        return profileDataStore.getProfile(firebaseUser.uid).map { localProfile ->
            UserProfile(
                uid = firebaseUser.uid,
                name = localProfile.name.ifBlank {
                    firebaseUser.name.orEmpty()
                },
                email = localProfile.email.ifBlank {
                    firebaseUser.email.orEmpty()
                },
                phoneNumber = localProfile.phoneNumber,
                address = localProfile.address,
                photoUri = localProfile.photoUri.ifBlank {
                    firebaseUser.photoUrl.orEmpty()
                },
            )
        }
    }

    suspend fun saveProfile(profile: UserProfile) {
        val firebaseUser = authRepository.getCurrentUser() ?: return
        profileDataStore.saveProfile(
            uid = firebaseUser.uid,
            profile = profile.copy(uid = firebaseUser.uid),
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileRepository? = null

        fun getInstance(
            profileDataStore: ProfileDataStore,
            authRepository: AuthRepository,
        ): ProfileRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = ProfileRepository(
                    profileDataStore = profileDataStore,
                    authRepository = authRepository,
                )
                INSTANCE = instance
                instance
            }
        }
    }
}