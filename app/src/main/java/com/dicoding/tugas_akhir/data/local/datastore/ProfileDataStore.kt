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

    val profileFlow: Flow<UserProfile> = context.profileDataStore.data.map { preferences ->
        UserProfile(
            name = preferences[NAME_KEY].orEmpty(),
            email = preferences[EMAIL_KEY].orEmpty(),
            phoneNumber = preferences[PHONE_KEY].orEmpty(),
            address = preferences[ADDRESS_KEY].orEmpty(),
            photoUri = preferences[PHOTO_URI_KEY].orEmpty(),
        )
    }

    suspend fun saveProfile(profile: UserProfile) {
        context.profileDataStore.edit { preferences ->
            preferences[NAME_KEY] = profile.name
            preferences[EMAIL_KEY] = profile.email
            preferences[PHONE_KEY] = profile.phoneNumber
            preferences[ADDRESS_KEY] = profile.address
            preferences[PHOTO_URI_KEY] = profile.photoUri
        }
    }

    companion object {
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PHONE_KEY = stringPreferencesKey("phone")
        private val ADDRESS_KEY = stringPreferencesKey("address")
        private val PHOTO_URI_KEY = stringPreferencesKey("photo_uri")

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