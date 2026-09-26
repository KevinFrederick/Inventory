package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.manager.ISyncManager
import javax.inject.Inject

class ScheduleBackgroundSyncUseCase @Inject constructor(
    private val syncManager: ISyncManager
) {
    operator fun invoke() {
        syncManager.schedulePeriodicSync()
    }
}