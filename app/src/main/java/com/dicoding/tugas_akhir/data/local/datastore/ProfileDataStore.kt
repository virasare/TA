package com.dicoding.tugas_akhir.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.tugas_akhir.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore by preferencesDataStore(
    name = "profile_preferences"
)

class ProfileDataStore private constructor(
    private val context: Context,
) {

    fun getProfile(uid: String): Flow<UserProfile> {
        return context.profileDataStore.data.map { preferences ->
            UserProfile(
                uid = uid,
                name = preferences[nameKey(uid)].orEmpty(),
                email = preferences[emailKey(uid)].orEmpty(),
                phoneNumber = preferences[phoneKey(uid)].orEmpty(),
                address = preferences[addressKey(uid)].orEmpty(),
                photoUri = preferences[photoUriKey(uid)].orEmpty(),
            )
        }
    }

    suspend fun saveProfile(
        uid: String,
        profile: UserProfile,
    ) {
        context.profileDataStore.edit { preferences ->
            preferences[nameKey(uid)] = profile.name
            preferences[emailKey(uid)] = profile.email
            preferences[phoneKey(uid)] = profile.phoneNumber
            preferences[addressKey(uid)] = profile.address
            preferences[photoUriKey(uid)] = profile.photoUri
        }
    }

    companion object {
        private fun nameKey(uid: String) = stringPreferencesKey("profile_${uid}_name")
        private fun emailKey(uid: String) = stringPreferencesKey("profile_${uid}_email")
        private fun phoneKey(uid: String) = stringPreferencesKey("profile_${uid}_phone")
        private fun addressKey(uid: String) = stringPreferencesKey("profile_${uid}_address")
        private fun photoUriKey(uid: String) = stringPreferencesKey("profile_${uid}_photo_uri")

        @Volatile
        private var INSTANCE: ProfileDataStore? = null

        fun getInstance(context: Context): ProfileDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = ProfileDataStore(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}