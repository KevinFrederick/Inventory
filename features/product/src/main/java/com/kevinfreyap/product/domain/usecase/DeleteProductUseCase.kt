package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.SessionError
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.domain.manager.ISyncManager
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.repository.IProductRepository
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val productRepository: IProductRepository,
    private val batchRepository: IStockBatchRepository,
    private val syncManager: ISyncManager,
    private val activeGroupSession: IActiveGroupSession,
) {
    suspend operator fun invoke(productId: ProductId): Result<Unit, SessionError> {
        val session = activeGroupSession.currentGroupId.first()
            ?: return Result.Error(SessionError.MISSING_GROUP)

        val groupId = GroupId(session)

        productRepository.deleteProduct(productId, groupId)
        batchRepository.deleteBatchesForProduct(productId, groupId)
        syncManager.triggerSync()

        return Result.Success(Unit)
    }
}