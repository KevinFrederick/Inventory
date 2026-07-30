package com.kevinfreyap.product.data.mapper

import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.relation.ProductWithDetails
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.ProductId

fun ProductWithDetails.toDomain(): Product {
    return Product(
        productId = ProductId(this.product.productId),
        category = this.category.toDomain(),
        location = this.location.toDomain(),
        name = this.product.name,
        description = this.product.description,
        barcode = this.product.barcode,
        sku = this.product.sku,
        quantity = this.product.quantity,
        price = this.product.price,
        imageUri = this.product.imageUri,
        expirationDate = this.product.expirationDate,
        minimumQuantity = this.product.minimumQuantity,
        supplier = this.product.supplier,
        createdAt = this.product.createdAt,
        lastUpdated = this.product.lastUpdated
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        productId = this.productId.value,
        categoryId = this.category.categoryId.value,
        locationId = this.location.locationId.value,
        name = this.name,
        description = this.description,
        barcode = this.barcode,
        sku = this.sku,
        quantity = this.quantity,
        price = this.price,
        imageUri = this.imageUri,
        expirationDate = this.expirationDate,
        minimumQuantity = this.minimumQuantity,
        supplier = this.supplier,
        createdAt = this.createdAt,
        lastUpdated = this.lastUpdated
    )
}

fun List<ProductWithDetails>.toDomainList(): List<Product> {
    return this.map { it.toDomain() }
}