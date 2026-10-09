package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.repository.IProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository: IProductRepository,
    private val activeGroupSession: IActiveGroupSession,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(productId: ProductId): Flow<Product?> {
        return activeGroupSession.currentGroupId.flatMapLatest { groupId ->
            if (groupId.isNullOrBlank()) {
                flowOf(null)
            } else {
                repository.getProductById(productId, GroupId(groupId))
            }
        }
    }
}