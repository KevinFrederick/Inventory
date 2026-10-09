package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevinfreyap.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    // User management
    @Query("SELECT userId FROM user LIMIT 1")
    suspend fun getLoggedInUserId(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: UserEntity)

    @Query("SELECT * FROM user LIMIT 1")
    fun observeUser(): Flow<UserEntity?>

    @Query("UPDATE user SET profilePictureUrl = :imageUrl")
    suspend fun updateProfilePicture(imageUrl: String)

    @Query("DELETE FROM user")
    suspend fun clearUser()

    @Query("UPDATE user SET profilePictureUrl = NULL")
    suspend fun deleteProfilePicture()
}