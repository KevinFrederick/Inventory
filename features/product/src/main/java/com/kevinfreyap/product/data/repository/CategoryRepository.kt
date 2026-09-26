package com.kevinfreyap.product.data.repository

import com.kevinfreyap.database.dao.CategoryDao
import com.kevinfreyap.database.model.SyncState
import com.kevinfreyap.product.data.mapper.toDomain
import com.kevinfreyap.product.data.mapper.toEntity
import com.kevinfreyap.product.domain.model.Category
import com.kevinfreyap.product.domain.repository.ICategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
): ICategoryRepository {
    override fun getAllCategory(): Flow<List<Category>> {
        return categoryDao.getAllCategory().map { categoryEntities ->
            categoryEntities.toDomain()
        }
    }

    override suspend fun getCategoryByName(name: String): Category? {
        return categoryDao.getCategoryByName(name)?.toDomain()
    }

    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category.toEntity(SyncState.CREATED))
    }
}