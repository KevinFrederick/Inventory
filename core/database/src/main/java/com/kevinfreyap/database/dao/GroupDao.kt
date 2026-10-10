package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kevinfreyap.database.entity.GroupEntity
import com.kevinfreyap.database.entity.GroupMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    // Group management
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateGroups(groups: List<GroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateGroupMembers(members: List<GroupMemberEntity>)

    @Query("SELECT COUNT(*) FROM `groups`")
    fun observeTotalGroupsCount(): Flow<Int>

    @Query("SELECT * FROM `groups`")
    fun observeGroups(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM `groups` ORDER BY createdAt DESC LIMIT 3")
    fun observePreviewGroups(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM `groups` WHERE groupId = :groupId")
    fun observeGroup(groupId: String): Flow<GroupEntity?>

    @Query("SELECT groupId, COUNT(*) as memberCount FROM group_members WHERE groupId IN (:groupIds) GROUP BY groupId")
    fun observeMemberCount(groupIds: List<String>): Flow<Map<@MapColumn(columnName="groupId") String, @MapColumn(columnName = "memberCount") Int>>

    @Query("SELECT * FROM group_members WHERE groupId = :groupId")
    fun getGroupMembers(groupId: String): Flow<List<GroupMemberEntity>>

    @Query("DELETE FROM group_members WHERE groupId = :groupId AND userId = :userId")
    suspend fun removeMember(groupId: String, userId: String)

    @Query("DELETE FROM `groups` WHERE groupId = :groupId")
    suspend fun deleteGroup(groupId: String)

    @Query("DELETE FROM `groups`")
    suspend fun clearGroups()

    @Transaction
    suspend fun replaceAllGroups(newGroups: List<GroupEntity>) {
        clearGroups()
        insertOrUpdateGroups(newGroups)
    }

    @Query("DELETE FROM group_members WHERE groupId = :groupId")
    suspend fun clearGroupMembers(groupId: String)

    @Transaction
    suspend fun replaceGroupMembers(groupId: String, newMembers: List<GroupMemberEntity>) {
        clearGroupMembers(groupId)
        insertOrUpdateGroupMembers(newMembers)
    }
}