package com.kevinfreyap.product.data.mapper

import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.model.SyncState
import com.kevinfreyap.product.data.network.dto.request.CategoryRequest
import com.kevinfreyap.product.data.network.dto.response.CategoryResponse
import com.kevinfreyap.product.domain.model.Category
import com.kevinfreyap.product.domain.model.CategoryId

fun CategoryEntity.toDomain(): Category {
    return Category(
        categoryId = CategoryId(this.categoryId),
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated
    )
}

fun List<CategoryEntity>.toDomain(): List<Category> {
    return this.map { entity ->
        entity.toDomain()
    }
}

fun Category.toEntity(syncState: SyncState): CategoryEntity {
    return CategoryEntity(
        categoryId = this.categoryId.value,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated,
        syncState = syncState
    )
}

// Sync
fun CategoryResponse.toEntity(): CategoryEntity {
    return CategoryEntity(
        categoryId = this.categoryId,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated,
        syncState = SyncState.SYNCED
    )
}

fun CategoryEntity.toRequest(): CategoryRequest {
    return CategoryRequest(
        categoryId = this.categoryId,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated
    )
}