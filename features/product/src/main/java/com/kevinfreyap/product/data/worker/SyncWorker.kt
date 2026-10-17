package com.kevinfreyap.product.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kevinfreyap.domain.repository.IUserGroupRepository
import com.kevinfreyap.product.domain.repository.ISyncRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlin.time.Duration.Companion.milliseconds

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncRepository: ISyncRepository,
    private val userGroupRepository: IUserGroupRepository,
): CoroutineWorker(appContext, workerParams){
    override suspend fun doWork(): Result {
        return try {
            val userGroups = userGroupRepository.getGroupsForUser().first()
            if (userGroups.isEmpty()) return Result.success()

            var allSuccess = true
            for (group in userGroups) {
                val syncResult = syncRepository.sync(group.groupId)
                if (syncResult !is com.kevinfreyap.domain.Result.Success) {
                    allSuccess = false
                }

                delay(1000.milliseconds)
            }

            if (allSuccess) {
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