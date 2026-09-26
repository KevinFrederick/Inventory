package com.kevinfreyap.database.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SyncPreferences @Inject constructor(
    private val datastore: DataStore<Preferences>
) {
    companion object {
        private val LAST_SYNC_KEY = longPreferencesKey("last_sync_timestamp")
    }

    suspend fun getLastSyncTimestamp(): Long {
        return datastore.data.map { preferences ->
            preferences[LAST_SYNC_KEY] ?: 0L
        }.first()
    }

    suspend fun saveLastSyncTimestamp(timestamp: Long) {
        datastore.edit { preferences ->
            preferences[LAST_SYNC_KEY] = timestamp
        }
    }
}