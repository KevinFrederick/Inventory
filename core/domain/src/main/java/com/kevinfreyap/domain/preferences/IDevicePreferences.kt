package com.kevinfreyap.domain.preferences

interface IDevicePreferences {
    suspend fun saveLastActiveGroup(userId: String, groupId: String)
    suspend fun getLastActiveGroup(userId: String): String?
}