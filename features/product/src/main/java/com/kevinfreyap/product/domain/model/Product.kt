package com.kevinfreyap.product.domain.model

data class Product(
    val productId: String,
    val category: Category,
    val name: String,
    val description: String?,
    val sku: String,
    val quantity: Int,
    val price: Double,
    val minimumQuantity: Int,
    val createdAt: Long,
    val lastUpdated: Long
)
