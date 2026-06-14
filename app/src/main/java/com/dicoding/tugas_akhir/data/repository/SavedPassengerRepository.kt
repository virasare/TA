package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.data.local.datastore.SavedPassengerDataStore
import com.dicoding.tugas_akhir.domain.model.SavedPassenger
import kotlinx.coroutines.flow.Flow

class SavedPassengerRepository private constructor(
    private val dataStore: SavedPassengerDataStore,
) {

    fun getSavedPassengers(): Flow<List<SavedPassenger>> {
        return dataStore.passengersFlow
    }

    suspend fun getSavedPassengerById(id: String): SavedPassenger? {
        return dataStore.getPassengerById(id)
    }

    suspend fun savePassenger(passenger: SavedPassenger) {
        dataStore.savePassenger(passenger)
    }

    suspend fun deletePassenger(passenger: SavedPassenger) {
        dataStore.deletePassenger(passenger)
    }

    companion object {
        @Volatile
        private var INSTANCE: SavedPassengerRepository? = null

        fun getInstance(
            dataStore: SavedPassengerDataStore,
        ): SavedPassengerRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = SavedPassengerRepository(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}