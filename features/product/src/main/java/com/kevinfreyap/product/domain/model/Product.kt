package com.kevinfreyap.product.domain.model

@JvmInline
value class ProductId(val value: String)

data class Product(
    val productId: ProductId,
    val category: Category,
    val location: Location,
    val name: String,
    val description: String?,
    val barcode: String?,
    val sku: String?,
    val quantity: Int,
    val price: Double,
    val imageUri: String?,
    val expirationDate: Long?,
    val minimumQuantity: Int,
    val supplier: String?,
    val createdAt: Long,
    val lastUpdated: Long
)
