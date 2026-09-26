package com.kevinfreyap.product.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kevinfreyap.product.domain.repository.ISyncRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncRepository: ISyncRepository
): CoroutineWorker(appContext, workerParams){
    override suspend fun doWork(): Result {
        return try {
            val result = syncRepository.sync()

            if (result is com.kevinfreyap.domain.Result.Success) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (_: Exception) {
            Result.retry()
        }
    }

}