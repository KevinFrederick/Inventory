package com.kevinfreyap.product.data.mapper

import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.relation.ProductWithDetails
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
        imageUri = this.product.imageUri,
        minimumQuantity = this.product.minimumQuantity,
        batches = this.batches.toDomain(),
        createdAt = this.product.createdAt,
        lastUpdated = this.product.lastUpdated
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        productId = this.productId.value,
        categoryId = this.category.categoryId.value,
        name = this.name,
        description = this.description,
        barcode = this.barcode,
        barcodeFormat = this.barcodeFormat,
        sku = this.sku,
        imageUri = this.imageUri,
        minimumQuantity = this.minimumQuantity,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated
    )
}

fun List<ProductWithDetails>.toDomainList(): List<Product> {
    return this.map { it.toDomain() }
}