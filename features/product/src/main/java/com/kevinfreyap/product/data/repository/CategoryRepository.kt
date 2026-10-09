package com.kevinfreyap.product.data.repository

import com.kevinfreyap.database.dao.CategoryDao
import com.kevinfreyap.database.model.SyncState
import com.kevinfreyap.domain.model.GroupId
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
    override fun getAllCategory(groupId: GroupId): Flow<List<Category>> {
        return categoryDao.getAllCategory(groupId = groupId.value).map { categoryEntities ->
            categoryEntities.toDomain()
        }
    }

    override suspend fun getCategoryByName(name: String, groupId: GroupId): Category? {
        return categoryDao.getCategoryByName(
            name = name,
            groupId = groupId.value
        )?.toDomain()
    }

    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category.toEntity(SyncState.CREATED))
    }
}