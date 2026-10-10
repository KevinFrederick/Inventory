package com.kevinfreyap.product.domain.repository

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.product.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    fun getAllCategory(groupId: GroupId): Flow<List<Category>>

    suspend fun getCategoryByName(name: String, groupId: GroupId): Category?

    suspend fun insertCategory(category: Category)
}