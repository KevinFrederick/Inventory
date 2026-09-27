package com.kevinfreyap.product.data.manager

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.kevinfreyap.product.data.worker.SyncWorker
import com.kevinfreyap.product.domain.manager.ISyncManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncManager @Inject constructor(
    @param:ApplicationContext private val context: Context
): ISyncManager {
    private val workManager by lazy { WorkManager.getInstance(context) }

    override fun triggerSync() {
        val workInfoFuture = workManager.getWorkInfosForUniqueWork("immediate_sync")
        val workInfos = workInfoFuture.get()

        val isRunning = workInfos.any { it.state == WorkInfo.State.RUNNING }

        if (isRunning) return

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWork = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniqueWork(
            "immediate_sync",
            ExistingWorkPolicy.REPLACE,
            syncWork
        )
    }

    override fun schedulePeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicSyncWork = PeriodicWorkRequestBuilder<SyncWorker>(
            4, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "periodic_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncWork
        )
    }
}