package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.SessionError
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.domain.manager.ISyncManager
import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteBatchUseCase @Inject constructor(
    private val repository: IStockBatchRepository,
    private val syncManager: ISyncManager,
    private val activeGroupSession: IActiveGroupSession,
) {
    suspend operator fun invoke(
        batchId: BatchId,
        productId: ProductId
    ): Result<Unit, SessionError> {
        val session = activeGroupSession.currentGroupId.first()
            ?: return Result.Error(SessionError.MISSING_GROUP)

        val groupId = GroupId(session)

        repository.deleteBatch(
            batchId = batchId,
            productId = productId,
            groupId = groupId,
            timestamp = System.currentTimeMillis()
        )
        syncManager.triggerSync()

        return Result.Success(Unit)
    }
}