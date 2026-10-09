package com.kevinfreyap.domain.manager

interface ISyncManager {
    fun triggerSync()
    fun schedulePeriodicSync()
    fun cancelAllWork()
}