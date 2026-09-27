package com.kevinfreyap.inventory

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.kevinfreyap.product.domain.usecase.ScheduleBackgroundSyncUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var scheduleBackgroundSync: ScheduleBackgroundSyncUseCase

    override fun onCreate() {
        super.onCreate()

        scheduleBackgroundSync()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}