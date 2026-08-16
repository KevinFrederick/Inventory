package com.kevinfreyap.product.data.mapper

import com.kevinfreyap.database.entity.StockBatchEntity
import com.kevinfreyap.database.entity.relation.BatchWithLocation
import com.kevinfreyap.database.entity.relation.BatchWithProductAndLocation
import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.StockBatch
import com.kevinfreyap.product.domain.model.StockBatchDetails

// Batches in Product
fun BatchWithLocation.toDomain(): StockBatch {
    return StockBatch(
        batchId = BatchId(this.batch.batchId),
        productId = ProductId(this.batch.productId),
        location = this.location.toDomain(),
        quantity = this.batch.quantity,
        price = this.batch.price,
        expirationDate = this.batch.expirationDate,
        supplier = this.batch.supplier,
        lastUpdated = this.batch.lastUpdated
    )
}

fun StockBatch.toEntity(): StockBatchEntity {
    return StockBatchEntity(
        batchId = this.batchId.value,
        productId = this.productId.value,
        locationId = this.location.locationId.value,
        quantity = this.quantity,
        expirationDate = this.expirationDate,
        price = this.price,
        supplier = this.supplier,
        lastUpdated = this.lastUpdated
    )
}

fun List<BatchWithLocation>.toDomain(): List<StockBatch> {
    return this.map { batchWithLocation -> 
        batchWithLocation.toDomain()
    }
}

// Batch by Id
fun BatchWithProductAndLocation.toDomain(): StockBatchDetails{
    return StockBatchDetails(
        batch = StockBatch(
            batchId = BatchId(this.batch.batchId),
            productId = ProductId(this.batch.productId),
            location = this.location.toDomain(),
            quantity = this.batch.quantity,
            price = this.batch.price,
            expirationDate = this.batch.expirationDate,
            supplier = this.batch.supplier,
            lastUpdated = this.batch.lastUpdated
        ),
        productName = this.product.name
    )
}