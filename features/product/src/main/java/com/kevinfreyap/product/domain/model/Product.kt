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
        get() = batches
            .map { it.price }
            .filter { it > 0.0 }
            .minOrNull()

    val maxBatchCost: Double?
        get() {
            val validPrices = batches
                .map { it.price }
                .filter { it > 0.0 }
            val min = validPrices.minOrNull()
            val max = validPrices.maxOrNull()

            return if (min == max) null else max
        }

    val nearestExpiringBatch: Long?
        get() = batches
            .mapNotNull { it.expirationDate }
            .minOrNull()

    val productTotalValue: Double?
        get() {
            val total = batches.sumOf { batch ->
                val batchItemCost = batch.price
                batch.quantity * batchItemCost
            }

            return if (total > 0.0) total else null
        }
}
