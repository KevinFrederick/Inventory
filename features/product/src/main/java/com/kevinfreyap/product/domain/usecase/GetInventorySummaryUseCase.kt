package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.product.domain.model.InventorySummary
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetInventorySummaryUseCase @Inject constructor(
    private val repository: IStockBatchRepository,
    private val activeGroupSession: IActiveGroupSession,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<InventorySummary> {
        return activeGroupSession.currentGroupId.flatMapLatest { groupId ->
            if (groupId.isNullOrBlank()) {
                flowOf(InventorySummary(
                    totalProduct = 0,
                    totalItem = 0,
                    totalValue = 0.0
                ))
            } else {
                repository.getInventorySummary(GroupId(groupId))
            }
        }
    }
}