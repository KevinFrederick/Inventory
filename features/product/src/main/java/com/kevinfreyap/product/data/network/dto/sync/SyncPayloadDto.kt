package com.kevinfreyap.product.data.network.dto.sync

import com.kevinfreyap.product.data.network.dto.request.CategoryRequest
import com.kevinfreyap.product.data.network.dto.request.LocationRequest
import kotlinx.serialization.Serializable

@Serializable
data class SyncPayloadDto(
    val createdCategories: List<CategoryRequest> = emptyList(),
    val updatedCategories: List<CategoryRequest> = emptyList(),
    val deletedCategories: List<String> = emptyList(),

    val createdLocations: List<LocationRequest> = emptyList(),
    val updatedLocations: List<LocationRequest> = emptyList(),
    val deletedLocations: List<String> = emptyList(),

    val createdProduct: List<SyncProductDto> = emptyList(),
    val updatedProduct: List<SyncProductDto> = emptyList(),
    val deletedProduct: List<String> = emptyList(),

    val createdBatches: List<SyncStockBatchDto> = emptyList(),
    val updatedBatches: List<SyncStockBatchDto> = emptyList(),
    val deletedBatches: List<String> = emptyList()
)
