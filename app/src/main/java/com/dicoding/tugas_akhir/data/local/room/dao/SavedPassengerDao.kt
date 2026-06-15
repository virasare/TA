package com.dicoding.tugas_akhir.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.tugas_akhir.data.local.room.entity.SavedPassengerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPassengerDao {

    @Query("SELECT * FROM saved_passengers ORDER BY createdAtMillis DESC")
    fun getSavedPassengers(): Flow<List<SavedPassengerEntity>>

    @Query("SELECT * FROM saved_passengers WHERE id = :id LIMIT 1")
    suspend fun getSavedPassengerById(id: String): SavedPassengerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedPassenger(
        passenger: SavedPassengerEntity,
    ): Long

    @Delete
    suspend fun deleteSavedPassenger(
        passenger: SavedPassengerEntity,
    ): Int
}