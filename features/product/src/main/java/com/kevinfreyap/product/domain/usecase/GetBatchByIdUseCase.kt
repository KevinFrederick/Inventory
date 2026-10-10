package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.StockBatch
import com.kevinfreyap.product.domain.model.StockBatchDetails
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetBatchByIdUseCase @Inject constructor(
    private val repository: IStockBatchRepository,
    private val activeGroupSession: IActiveGroupSession
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(batchId: BatchId): Flow<StockBatchDetails?> {
        return activeGroupSession.currentGroupId.flatMapLatest { groupId ->
            if (groupId.isNullOrBlank()) {
                flowOf(null)
            } else {
                repository.getBatchById(batchId, GroupId(groupId))
            }
        }
    }
}