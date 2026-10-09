package com.kevinfreyap.product.domain.usecase

import androidx.paging.PagingData
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.product.domain.model.FilteredPagingStream
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.query.ProductQueryFilter
import com.kevinfreyap.product.domain.repository.IProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetFilteredProductUseCase @Inject constructor (
    private val repository: IProductRepository,
    private val activeGroupSession: IActiveGroupSession,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(filterProvider: () -> ProductQueryFilter): FilteredPagingStream<Product> {
        var currentInvalidate: (() -> Unit)? = null

        val pagingFlow = activeGroupSession.currentGroupId.flatMapLatest { session ->
            if (session.isNullOrBlank()) {
                flowOf(PagingData.empty())
            } else {
                val stream = repository.getProductStream(
                    filterProvider = filterProvider,
                    groupId = GroupId(session)
                )
                currentInvalidate = stream.invalidate
                stream.flow
            }
        }

        return FilteredPagingStream(
            flow = pagingFlow,
            invalidate = { currentInvalidate?.invoke() }
        )
    }
}