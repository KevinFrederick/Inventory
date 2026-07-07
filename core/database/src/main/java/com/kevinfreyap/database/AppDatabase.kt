package com.kevinfreyap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kevinfreyap.database.dao.CategoryDao
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.dao.TransactionDao
import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.TransactionEntity

@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao
}