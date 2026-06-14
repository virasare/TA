package com.dicoding.tugas_akhir.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.tugas_akhir.domain.model.SavedPassenger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.net.URLDecoder
import java.net.URLEncoder

private val Context.savedPassengerDataStore by preferencesDataStore(
    name = "saved_passenger_preferences"
)

class SavedPassengerDataStore private constructor(
    private val context: Context,
) {

    val passengersFlow: Flow<List<SavedPassenger>> =
        context.savedPassengerDataStore.data.map { preferences ->
            val rawValue = preferences[PASSENGERS_KEY].orEmpty()
            decodePassengers(rawValue)
        }

    suspend fun savePassenger(passenger: SavedPassenger) {
        val currentPassengers = passengersFlow.first()

        val updatedPassengers = currentPassengers
            .filterNot { it.id == passenger.id } + passenger

        context.savedPassengerDataStore.edit { preferences ->
            preferences[PASSENGERS_KEY] = encodePassengers(updatedPassengers)
        }
    }

    suspend fun deletePassenger(passenger: SavedPassenger) {
        val currentPassengers = passengersFlow.first()

        val updatedPassengers = currentPassengers.filterNot {
            it.id == passenger.id
        }

        context.savedPassengerDataStore.edit { preferences ->
            preferences[PASSENGERS_KEY] = encodePassengers(updatedPassengers)
        }
    }

    suspend fun getPassengerById(id: String): SavedPassenger? {
        return passengersFlow.first().find { it.id == id }
    }

    private fun encodePassengers(passengers: List<SavedPassenger>): String {
        return passengers.joinToString(ROW_SEPARATOR) { passenger ->
            listOf(
                passenger.id,
                passenger.fullName,
                passenger.nik,
                passenger.phoneNumber,
                passenger.gender,
            ).joinToString(COLUMN_SEPARATOR) { value ->
                encode(value)
            }
        }
    }

    private fun decodePassengers(rawValue: String): List<SavedPassenger> {
        if (rawValue.isBlank()) return emptyList()

        return rawValue.split(ROW_SEPARATOR)
            .mapNotNull { row ->
                val columns = row.split(COLUMN_SEPARATOR)

                if (columns.size < 5) {
                    null
                } else {
                    SavedPassenger(
                        id = decode(columns[0]),
                        fullName = decode(columns[1]),
                        nik = decode(columns[2]),
                        phoneNumber = decode(columns[3]),
                        gender = decode(columns[4]),
                    )
                }
            }
    }

    private fun encode(value: String): String {
        return URLEncoder.encode(value, "UTF-8")
    }

    private fun decode(value: String): String {
        return URLDecoder.decode(value, "UTF-8")
    }

    companion object {
        private val PASSENGERS_KEY = stringPreferencesKey("saved_passengers")
        private const val ROW_SEPARATOR = ";;ROW;;"
        private const val COLUMN_SEPARATOR = ";;COL;;"

        @Volatile
        private var INSTANCE: SavedPassengerDataStore? = null

        fun getInstance(context: Context): SavedPassengerDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = SavedPassengerDataStore(
                    context = context.applicationContext,
                )
                INSTANCE = instance
                instance
            }
        }
    }
}