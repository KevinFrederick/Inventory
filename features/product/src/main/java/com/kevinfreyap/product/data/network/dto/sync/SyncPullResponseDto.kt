package com.kevinfreyap.product.data.network.dto.sync

import com.kevinfreyap.product.data.network.dto.response.CategoryResponse
import com.kevinfreyap.product.data.network.dto.response.LocationResponse
import kotlinx.serialization.Serializable

@Serializable
data class SyncPullResponseDto(
    val categories: List<CategoryResponse>,
    val locations: List<LocationResponse>,
    val products: List<SyncProductDto>,
    val batches: List<SyncStockBatchDto>,

    val deletedCategories: List<String>,
    val deletedLocations: List<String>,
    val deletedProducts: List<String>,
    val deletedBatches: List<String>,

    val serverTimeStamp: Long,
)
