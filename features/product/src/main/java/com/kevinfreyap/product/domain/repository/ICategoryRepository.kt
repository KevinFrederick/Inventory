package com.kevinfreyap.product.domain.repository

import com.kevinfreyap.product.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    fun getAllCategory(): Flow<List<Category>>

    suspend fun getCategoryByName(name: String): Category?

    suspend fun insertCategory(category: Category)
}