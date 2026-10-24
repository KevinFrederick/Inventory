package com.kevinfreyap.database.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kevinfreyap.database.di.DeviceDataStore
import com.kevinfreyap.domain.preferences.IDevicePreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DevicePreferences @Inject constructor(
    @param:DeviceDataStore private val datastore: DataStore<Preferences>
): IDevicePreferences {
    companion object {
        private fun lastGroupKey(userId: String) = stringPreferencesKey("last_group_for_$userId")
    }

    override suspend fun saveLastActiveGroup(
        userId: String,
        groupId: String
    ) {
        val key = lastGroupKey(userId)
        datastore.edit { preferences ->
            preferences[key] = groupId
        }
    }

    override suspend fun getLastActiveGroup(userId: String): String? {
        val key = lastGroupKey(userId)
        val preferences = datastore.data.first()
        return preferences[key]
    }
}