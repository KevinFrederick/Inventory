package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.kevinfreyap.database.entity.StockBatchEntity
import com.kevinfreyap.database.entity.relation.BatchWithProductAndLocation
import com.kevinfreyap.database.model.InventorySummaryDb
import com.kevinfreyap.database.model.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface BatchDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertBatch(batch: StockBatchEntity)

    @Transaction
    @Query("SELECT * FROM stock_batch WHERE batchId = :id AND syncState != 'DELETED'")
    fun getBatchById(id: String): Flow<BatchWithProductAndLocation?>

    @Transaction
    @Query("SELECT * FROM stock_batch WHERE batchId = :id")
    suspend fun getBatchSnapshot(id: String): BatchWithProductAndLocation?

    @Transaction
    @Query("SELECT * FROM stock_batch WHERE productId = :id")
    suspend fun getBatchesByProductId(id: String): List<BatchWithProductAndLocation>

    @Query("SELECT * FROM stock_batch WHERE expirationDate IS NOT NULL AND syncState != 'DELETED' ORDER BY expirationDate ASC LIMIT :limit")
    fun getExpiringBatches(limit: Int = 10): Flow<List<StockBatchEntity>>

    @Query("SELECT * FROM stock_batch WHERE locationId = :locationId AND syncState != 'DELETED'")
    fun getBatchesByLocation(locationId: String): Flow<List<StockBatchEntity>>

    @Query("""
        SELECT 
            (SELECT COUNT(*) FROM product WHERE syncState != 'DELETED') AS totalProduct,
            COALESCE(SUM(quantity), 0) AS totalItem,
            COALESCE(SUM(quantity * COALESCE(price, 0)), 0.0) AS totalValue
        FROM stock_batch
        WHERE syncState != 'DELETED'
    """)
    fun getInventorySummary(): Flow<InventorySummaryDb>

    @Update
    suspend fun updateBatch(batch: StockBatchEntity): Int

    @Query("DELETE FROM stock_batch WHERE batchId = :id")
    suspend fun deleteBatch(id: String)

    // Sync
    @Upsert
    suspend fun upsertAll(batches: List<StockBatchEntity>)

    @Query("DELETE FROM stock_batch WHERE batchId IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Query("SELECT * FROM stock_batch WHERE syncState != 'SYNCED'")
    suspend fun getUnsyncedBatches(): List<StockBatchEntity>

    @Query("DELETE FROM stock_batch WHERE batchId IN (:ids) AND syncState = 'DELETED'")
    suspend fun clearTombstones(ids: List<String>)

    @Query("UPDATE stock_batch SET syncState = 'SYNCED' WHERE batchId IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Query("UPDATE stock_batch SET syncState = :state WHERE batchId = :id")
    suspend fun markAsDeleted(id: String, state: SyncState)
}