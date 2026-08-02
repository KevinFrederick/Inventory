package com.kevinfreyap.database

import com.kevinfreyap.database.TestData.batch1Prod1
import com.kevinfreyap.database.TestData.batch1Prod2
import com.kevinfreyap.database.TestData.batch1Prod3
import com.kevinfreyap.database.TestData.batch2Prod1
import com.kevinfreyap.database.TestData.batch2Prod3
import com.kevinfreyap.database.TestData.category1
import com.kevinfreyap.database.TestData.category2
import com.kevinfreyap.database.TestData.category3
import com.kevinfreyap.database.TestData.location1
import com.kevinfreyap.database.TestData.location2
import com.kevinfreyap.database.TestData.prod1
import com.kevinfreyap.database.TestData.prod2
import com.kevinfreyap.database.TestData.prod3
import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.StockBatchEntity

object TestData {
    val category1 = CategoryEntity(
        categoryId = "electronic",
        name = "Electronic",
        description = null,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val category2 = CategoryEntity(
        categoryId = "furniture",
        name = "Furniture",
        description = null,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val category3 = CategoryEntity(
        categoryId = "food",
        name = "Food",
        description = null,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val prod1 = ProductEntity(
        productId = "prod-01",
        categoryId = "electronic",
        name = "Laptop",
        description = null,
        barcode = "123456789",
        sku = "bgr-01",
        imageUri = null,
        minimumQuantity = 5,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val prod2 = ProductEntity(
        productId = "prod-02",
        categoryId = "furniture",
        name = "Desk",
        description = null,
        barcode = "123456789",
        sku = "bgr-02",
        imageUri = null,
        minimumQuantity = 2,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val prod3 = ProductEntity(
        productId = "prod-03",
        categoryId = "food",
        name = "Milk",
        description = null,
        barcode = "123456789",
        sku = null,
        imageUri = null,
        minimumQuantity = 2,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val batch1Prod1 = StockBatchEntity(
        batchId = "batch-01",
        productId = "prod-01",
        locationId = "loc-01",
        quantity = 2,
        expirationDate = null,
        price = 1500.0,
        supplier = null,
        lastUpdated = 1000L
    )

    val batch2Prod1 = StockBatchEntity(
        batchId = "batch-02",
        productId = "prod-01",
        locationId = "loc-01",
        quantity = 3,
        expirationDate = null,
        price = 1500.0,
        supplier = null,
        lastUpdated = 1000L
    )

    val batch1Prod2 = StockBatchEntity(
        batchId = "batch-03",
        productId = "prod-02",
        locationId = "loc-01",
        quantity = 5,
        expirationDate = null,
        price = 1500.0,
        supplier = null,
        lastUpdated = 1000L
    )

    val batch1Prod3 = StockBatchEntity(
        batchId = "batch-04",
        productId = "prod-03",
        locationId = "loc-02",
        quantity = 5,
        expirationDate = null,
        price = 1500.0,
        supplier = null,
        lastUpdated = 1000L
    )

    val batch2Prod3 = StockBatchEntity(
        batchId = "batch-05",
        productId = "prod-03",
        locationId = "loc-01",
        quantity = 2,
        expirationDate = null,
        price = 15.0,
        supplier = null,
        lastUpdated = 1000L
    )

    val location1 = LocationEntity(
        locationId = "loc-01",
        name = "Garage",
        description = null,
        locationBarcode = null,
        createdAt = 1000L,
        lastUpdated = 1000L,
    )

    val location2 = LocationEntity(
        locationId = "loc-02",
        name = "Kitchen",
        description = null,
        locationBarcode = null,
        createdAt = 1000L,
        lastUpdated = 1000L,
    )
}

suspend fun AppDatabase.populateWithData() {
    this.locationDao().insertLocation(location1)
    this.locationDao().insertLocation(location2)

    this.categoryDao().insertCategory(category1)
    this.categoryDao().insertCategory(category2)
    this.categoryDao().insertCategory(category3)

    this.productDao().insertProduct(prod1)
    this.productDao().insertProduct(prod2)
    this.productDao().insertProduct(prod3)

    this.batchDao().insertBatch(batch1Prod1)
    this.batchDao().insertBatch(batch2Prod1)
    this.batchDao().insertBatch(batch1Prod2)
    this.batchDao().insertBatch(batch1Prod3)
    this.batchDao().insertBatch(batch2Prod3)
}