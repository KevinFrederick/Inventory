package com.kevinfreyap.database

import androidx.room.withTransaction
import com.kevinfreyap.database.TestData.batch1Prod1
import com.kevinfreyap.database.TestData.batch1Prod2
import com.kevinfreyap.database.TestData.batch1Prod3
import com.kevinfreyap.database.TestData.batch2Prod1
import com.kevinfreyap.database.TestData.batch2Prod3
import com.kevinfreyap.database.TestData.category1
import com.kevinfreyap.database.TestData.category2
import com.kevinfreyap.database.TestData.category3
import com.kevinfreyap.database.TestData.group1
import com.kevinfreyap.database.TestData.location1
import com.kevinfreyap.database.TestData.location2
import com.kevinfreyap.database.TestData.prod1
import com.kevinfreyap.database.TestData.prod2
import com.kevinfreyap.database.TestData.prod3
import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.entity.GroupEntity
import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.StockBatchEntity

object TestData {
    val group1 = GroupEntity(
        groupId = "group1",
        name = "Group 1",
        description = null,
        address = null,
        currentUserRole = "OWNER",
        createdAt = 1000L,
        lastUpdated = 1000L
    )
    val category1 = CategoryEntity(
        categoryId = "electronic",
        groupId = "group1",
        name = "Electronic",
        description = null,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val category2 = CategoryEntity(
        categoryId = "furniture",
        groupId = "group1",
        name = "Furniture",
        description = null,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val category3 = CategoryEntity(
        groupId = "group1",
        categoryId = "food",
        name = "Food",
        description = null,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val prod1 = ProductEntity(
        productId = "prod-01",
        categoryId = "electronic",
        groupId = "group1",
        name = "Laptop",
        description = null,
        barcode = "111111111",
        barcodeFormat = "CODE_128",
        sku = "bgr-01",
        localImagePath = null,
        remoteImageUrl = null,
        minimumQuantity = 5,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val prod2 = ProductEntity(
        productId = "prod-02",
        categoryId = "furniture",
        groupId = "group1",
        name = "Desk",
        description = null,
        barcode = "222222222",
        barcodeFormat = "CODE_128",
        sku = "bgr-02",
        localImagePath = null,
        remoteImageUrl = null,
        minimumQuantity = 2,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val prod3 = ProductEntity(
        productId = "prod-03",
        categoryId = "food",
        groupId = "group1",
        name = "Milk",
        description = null,
        barcode = "333333333",
        barcodeFormat = "CODE_128",
        sku = null,
        localImagePath = null,
        remoteImageUrl = null,
        minimumQuantity = 2,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val batch1Prod1 = StockBatchEntity(
        batchId = "batch-01",
        productId = "prod-01",
        locationId = "loc-01",
        groupId = "group1",
        quantity = 2,
        expirationDate = null,
        price = 1500.0,
        supplier = null,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val batch2Prod1 = StockBatchEntity(
        batchId = "batch-02",
        productId = "prod-01",
        locationId = "loc-01",
        groupId = "group1",
        quantity = 3,
        expirationDate = null,
        price = 1500.0,
        supplier = null,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val batch1Prod2 = StockBatchEntity(
        batchId = "batch-03",
        productId = "prod-02",
        locationId = "loc-01",
        groupId = "group1",
        quantity = 5,
        expirationDate = null,
        price = 1500.0,
        supplier = null,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val batch1Prod3 = StockBatchEntity(
        batchId = "batch-04",
        productId = "prod-03",
        locationId = "loc-02",
        groupId = "group1",
        quantity = 5,
        expirationDate = null,
        price = 1500.0,
        supplier = null,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val batch2Prod3 = StockBatchEntity(
        batchId = "batch-05",
        productId = "prod-03",
        locationId = "loc-01",
        groupId = "group1",
        quantity = 2,
        expirationDate = null,
        price = 15.0,
        supplier = null,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val location1 = LocationEntity(
        locationId = "loc-01",
        groupId = "group1",
        name = "Garage",
        description = null,
        locationBarcode = null,
        createdAt = 1000L,
        lastUpdated = 1000L,
    )

    val location2 = LocationEntity(
        locationId = "loc-02",
        groupId = "group1",
        name = "Kitchen",
        description = null,
        locationBarcode = null,
        createdAt = 1000L,
        lastUpdated = 1000L,
    )
}

suspend fun AppDatabase.populateWithData() {
    this.withTransaction {
        groupDao().insertOrUpdateGroups(listOf(group1))

        locationDao().insertLocation(location1)
        locationDao().insertLocation(location2)

        categoryDao().insertCategory(category1)
        categoryDao().insertCategory(category2)
        categoryDao().insertCategory(category3)

        productDao().insertProduct(prod1)
        productDao().insertProduct(prod2)
        productDao().insertProduct(prod3)

        batchDao().insertBatch(batch1Prod1)
        batchDao().insertBatch(batch2Prod1)
        batchDao().insertBatch(batch1Prod2)
        batchDao().insertBatch(batch1Prod3)
        batchDao().insertBatch(batch2Prod3)
    }
}