package com.kevinfreyap.product.data.mapper

import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.relation.ProductWithDetails
import com.kevinfreyap.database.model.SyncState
import com.kevinfreyap.product.data.network.dto.sync.SyncProductDto
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.ProductId

fun ProductWithDetails.toDomain(): Product {
    return Product(
        productId = ProductId(this.product.productId),
        category = this.category.toDomain(),
        name = this.product.name,
        description = this.product.description,
        barcode = this.product.barcode,
        barcodeFormat = this.product.barcodeFormat,
        sku = this.product.sku,
        localImagePath = this.product.localImagePath,
        remoteImageUrl = this.product.remoteImageUrl,
        minimumQuantity = this.product.minimumQuantity,
        batches = this.batches.toDomain(),
        createdAt = this.product.createdAt,
        lastUpdated = this.product.lastUpdated
    )
}

fun Product.toEntity(syncState: SyncState): ProductEntity {
    return ProductEntity(
        productId = this.productId.value,
        categoryId = this.category.categoryId.value,
        name = this.name,
        description = this.description,
        barcode = this.barcode,
        barcodeFormat = this.barcodeFormat,
        sku = this.sku,
        localImagePath = this.localImagePath,
        remoteImageUrl = this.remoteImageUrl,
        minimumQuantity = this.minimumQuantity,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated,
        syncState = syncState
    )
}

fun List<ProductWithDetails>.toDomainList(): List<Product> {
    return this.map { it.toDomain() }
}

// Sync
fun SyncProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        productId = this.productId,
        categoryId = this.categoryId,
        name = this.name,
        description = this.description,
        barcode = this.barcode,
        barcodeFormat = this.barcodeFormat,
        sku = this.sku,
        localImagePath = null,
        remoteImageUrl = this.imageUri,
        minimumQuantity = this.minimumQuantity,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated,
        syncState = SyncState.SYNCED
    )
}

fun ProductEntity.toRequest(): SyncProductDto {
    return SyncProductDto(
        productId = this.productId,
        categoryId = this.categoryId,
        name = this.name,
        description = this.description,
        barcode = this.barcode,
        barcodeFormat = this.barcodeFormat,
        sku = this.sku,
        imageUri = this.remoteImageUrl,
        minimumQuantity = this.minimumQuantity,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated
    )
}