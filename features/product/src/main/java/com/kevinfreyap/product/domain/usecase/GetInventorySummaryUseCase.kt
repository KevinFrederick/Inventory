package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.InventorySummary
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInventorySummaryUseCase @Inject constructor(
    private val repository: IStockBatchRepository
) {
    operator fun invoke(): Flow<InventorySummary> {
        return repository.getInventorySummary()
    }
}