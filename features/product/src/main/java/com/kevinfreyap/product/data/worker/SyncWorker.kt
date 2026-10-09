package com.kevinfreyap.product.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.product.domain.repository.ISyncRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncRepository: ISyncRepository,
    private val activeGroupSession: IActiveGroupSession
): CoroutineWorker(appContext, workerParams){
    override suspend fun doWork(): Result {
        return try {
            val groupId = activeGroupSession.currentGroupId.first()
                ?: return Result.failure()

            val result = syncRepository.sync(GroupId(groupId))

            if (result is com.kevinfreyap.domain.Result.Success) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("SyncWorkerDebug", "Worker failed with exception: ${e.message}", e)
            Result.retry()
        }
    }
}