package com.kevinfreyap.database.session

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kevinfreyap.domain.session.IActiveGroupSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ActiveGroupSession @Inject constructor(
    private val dataStore: DataStore<Preferences>
): IActiveGroupSession {
    private val GROUP_ID_KEY = stringPreferencesKey("active_group_id")

    override val currentGroupId: Flow<String?> = dataStore.data
        .catch { exception->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[GROUP_ID_KEY]
        }

    override suspend fun setActiveGroup(groupId: String) {
        dataStore.edit { preferences ->
            preferences[GROUP_ID_KEY] = groupId
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(GROUP_ID_KEY)
        }
    }
}