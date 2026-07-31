package com.kevinfreyap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kevinfreyap.database.dao.BatchDao
import com.kevinfreyap.database.dao.CategoryDao
import com.kevinfreyap.database.dao.LocationDao
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.dao.TransactionDao
import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.StockBatchEntity
import com.kevinfreyap.database.entity.TransactionEntity
import com.kevinfreyap.database.entity.TransactionItemEntity

@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class,
        TransactionEntity::class,
        TransactionItemEntity::class,
        LocationEntity::class,
        StockBatchEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao
    abstract fun locationDao(): LocationDao
    abstract fun batchDao(): BatchDao
}