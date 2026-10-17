package com.kevinfreyap.database.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.kevinfreyap.domain.preferences.ISyncPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SyncPreferences @Inject constructor(
    private val datastore: DataStore<Preferences>
): ISyncPreferences {
    companion object {
        private fun lastSyncKey(groupId: String) = longPreferencesKey("last_sync_timestamp_$groupId")
    }

    override suspend fun getLastSyncTimestamp(groupId: String): Long {
        val key = lastSyncKey(groupId)
        return datastore.data.map { preferences ->
            preferences[key] ?: 0L
        }.first()
    }

    override suspend fun saveLastSyncTimestamp(groupId: String, timestamp: Long) {
        val key = lastSyncKey(groupId)
        datastore.edit { preferences ->
            preferences[key] = timestamp
        }
    }

    override suspend fun clearAll() {
        datastore.edit { preferences ->
            preferences.clear()
        }
    }
}