package com.kevinfreyap.product.domain.model

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

data class FilteredPagingStream<T: Any>(
    val flow: Flow<PagingData<T>>,
    val invalidate: () -> Unit
)