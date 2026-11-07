package com.kevinfreyap.database.manager

import com.kevinfreyap.database.AppDatabase
import com.kevinfreyap.domain.manager.ISessionCleaner
import com.kevinfreyap.domain.manager.ISyncManager
import com.kevinfreyap.domain.preferences.ISyncPreferences
import com.kevinfreyap.domain.session.IActiveGroupSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SessionCleaner @Inject constructor(
    private val database: AppDatabase,
    private val activeGroupSession: IActiveGroupSession,
    private val syncPreferences: ISyncPreferences,
    private val syncManager: ISyncManager
): ISessionCleaner {
    override suspend fun clearSessionData() {
        withContext(Dispatchers.IO) {
            activeGroupSession.clear()
            syncPreferences.clearAll()
            syncManager.cancelAllWork()
            database.clearAllTables()
        }
    }
}