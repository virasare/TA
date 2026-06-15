package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.data.local.room.dao.SavedPassengerDao
import com.dicoding.tugas_akhir.data.mapper.DataMapper
import com.dicoding.tugas_akhir.domain.model.SavedPassenger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedPassengerRepository private constructor(
    private val savedPassengerDao: SavedPassengerDao,
) {

    fun getSavedPassengers(): Flow<List<SavedPassenger>> {
        return savedPassengerDao.getSavedPassengers().map { entities ->
            entities.map {
                DataMapper.mapSavedPassengerEntityToDomain(it)
            }
        }
    }

    suspend fun getSavedPassengerById(id: String): SavedPassenger? {
        return savedPassengerDao.getSavedPassengerById(id)?.let {
            DataMapper.mapSavedPassengerEntityToDomain(it)
        }
    }

    suspend fun savePassenger(passenger: SavedPassenger) {
        savedPassengerDao.insertSavedPassenger(
            DataMapper.mapSavedPassengerDomainToEntity(passenger)
        )
    }

    suspend fun deletePassenger(passenger: SavedPassenger) {
        savedPassengerDao.deleteSavedPassenger(
            DataMapper.mapSavedPassengerDomainToEntity(passenger)
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: SavedPassengerRepository? = null

        fun getInstance(
            savedPassengerDao: SavedPassengerDao,
        ): SavedPassengerRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = SavedPassengerRepository(savedPassengerDao)
                INSTANCE = instance
                instance
            }
        }
    }
}