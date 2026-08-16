package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kevinfreyap.database.entity.StockBatchEntity
import com.kevinfreyap.database.entity.relation.BatchWithLocation
import com.kevinfreyap.database.entity.relation.BatchWithProductAndLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface BatchDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertBatch(batch: StockBatchEntity)

    @Transaction
    @Query("SELECT * FROM stock_batch WHERE batchId = :id")
    fun getBatchById(id: String): Flow<BatchWithProductAndLocation?>

    @Query("SELECT * FROM stock_batch WHERE expirationDate IS NOT NULL ORDER BY expirationDate ASC LIMIT :limit")
    fun getExpiringBatches(limit: Int = 10): Flow<List<StockBatchEntity>>

    @Query("SELECT * FROM stock_batch WHERE locationId = :locationId")
    fun getBatchesByLocation(locationId: String): Flow<List<StockBatchEntity>>

    @Update
    suspend fun updateBatch(batch: StockBatchEntity): Int

    @Query("DELETE FROM stock_batch WHERE batchId = :id")
    suspend fun deleteBatch(id: String)
}