package com.kevinfreyap.product.domain.model

@JvmInline
value class ProductId(val value: String)

data class Product(
    val productId: ProductId,
    val category: Category,
    val name: String,
    val description: String?,
    val barcode: String?,
    val sku: String?,
    val imageUri: String?,
    val minimumQuantity: Int,
    val batches: List<StockBatch>,
    val createdAt: Long,
    val lastUpdated: Long
) {
    val totalQuantity: Int
        get() = batches.sumOf { it.quantity }

    val minBatchCost: Double?
        get() = batches.minOfOrNull { it.price }

    val maxBatchCost: Double?
        get() {
            val min = batches.minOfOrNull { it.price }
            val max = batches.maxOfOrNull { it.price }

            return if (min == max) null else max
        }

    val nearestExpiringBatch: Long?
        get() = batches
            .filter { it.expirationDate != null }
            .minOfOrNull { it.expirationDate!! }
}
