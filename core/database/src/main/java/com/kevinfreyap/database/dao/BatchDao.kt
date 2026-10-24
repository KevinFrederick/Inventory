package com.kevinfreyap.database.dao

import androidx.room.Dao
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
    @Upsert
    suspend fun insertBatch(batch: StockBatchEntity)

    @Transaction
    @Query("SELECT * FROM stock_batch WHERE batchId = :id AND syncState != 'DELETED' AND groupId = :groupId")
    fun getBatchById(id: String, groupId: String): Flow<BatchWithProductAndLocation?>

    @Transaction
    @Query("SELECT * FROM stock_batch WHERE batchId = :id AND groupId = :groupId")
    suspend fun getBatchSnapshot(id: String, groupId: String): BatchWithProductAndLocation?

    @Transaction
    @Query("SELECT * FROM stock_batch WHERE productId = :id AND groupId = :groupId")
    suspend fun getBatchesByProductId(id: String, groupId: String): List<BatchWithProductAndLocation>

    @Query("SELECT * FROM stock_batch WHERE expirationDate IS NOT NULL AND syncState != 'DELETED' AND groupId = :groupId ORDER BY expirationDate ASC LIMIT :limit")
    fun getExpiringBatches(limit: Int = 10, groupId: String): Flow<List<StockBatchEntity>>

    @Query("SELECT * FROM stock_batch WHERE locationId = :locationId AND syncState != 'DELETED' AND groupId = :groupId")
    fun getBatchesByLocation(locationId: String, groupId: String): Flow<List<StockBatchEntity>>

    @Query("""
        SELECT 
            (SELECT COUNT(*) FROM product WHERE syncState != 'DELETED' AND groupId = :groupId) AS totalProduct,
            COALESCE(SUM(quantity), 0) AS totalItem,
            COALESCE(SUM(quantity * COALESCE(price, 0)), 0.0) AS totalValue
        FROM stock_batch
        WHERE syncState != 'DELETED' AND groupId = :groupId
    """)
    fun getInventorySummary(groupId: String): Flow<InventorySummaryDb>

    @Update
    suspend fun updateBatch(batch: StockBatchEntity): Int

    @Query("DELETE FROM stock_batch WHERE batchId = :id AND groupId = :groupId")
    suspend fun deleteBatch(id: String, groupId: String)

    // Sync
    @Upsert
    suspend fun upsertAll(batches: List<StockBatchEntity>)

    @Query("DELETE FROM stock_batch WHERE batchId IN (:ids) AND groupId = :groupId")
    suspend fun deleteByIds(ids: List<String>, groupId: String)

    @Query("SELECT * FROM stock_batch WHERE syncState != 'SYNCED' AND groupId = :groupId")
    suspend fun getUnsyncedBatches(groupId: String): List<StockBatchEntity>

    @Query("DELETE FROM stock_batch WHERE batchId IN (:ids) AND syncState = 'DELETED' AND groupId = :groupId")
    suspend fun clearTombstones(ids: List<String>, groupId: String)

    @Query("UPDATE stock_batch SET syncState = 'SYNCED' WHERE batchId IN (:ids) AND groupId = :groupId")
    suspend fun markAsSynced(ids: List<String>, groupId: String)

    @Query("UPDATE stock_batch SET syncState = :state WHERE batchId = :id AND groupId = :groupId")
    suspend fun markAsDeleted(id: String, state: SyncState, groupId: String)
}