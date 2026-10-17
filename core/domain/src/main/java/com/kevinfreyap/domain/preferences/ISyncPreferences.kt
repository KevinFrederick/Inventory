package com.kevinfreyap.domain.preferences

interface ISyncPreferences {
    suspend fun getLastSyncTimestamp(groupId: String): Long
    suspend fun saveLastSyncTimestamp(groupId: String, timestamp: Long)
    suspend fun clearAll()
}