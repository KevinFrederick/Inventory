package com.kevinfreyap.product.domain.manager

interface ISyncManager {
    fun triggerSync()
    fun schedulePeriodicSync()
}