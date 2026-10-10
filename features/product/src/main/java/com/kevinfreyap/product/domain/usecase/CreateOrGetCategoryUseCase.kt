package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.manager.ISyncManager
import com.kevinfreyap.product.domain.model.Category
import com.kevinfreyap.product.domain.model.CategoryId
import com.kevinfreyap.product.domain.repository.ICategoryRepository
import java.util.UUID
import javax.inject.Inject

class CreateOrGetCategoryUseCase @Inject constructor(
    private val repository: ICategoryRepository,
    private val syncManager: ISyncManager,
) {
    suspend operator fun invoke(validCategoryName: String, groupId: GroupId): Category {
        val existingCategory = repository.getCategoryByName(validCategoryName, groupId)

        return if (existingCategory != null) {
            existingCategory
        } else {
            val newCategory = Category(
                categoryId = CategoryId("category-${UUID.randomUUID()}"),
                groupId = groupId,
                name = validCategoryName,
                description = null,
                createdAt = System.currentTimeMillis(),
                lastUpdated = System.currentTimeMillis()
            )
            repository.insertCategory(newCategory)
            syncManager.triggerSync()
            newCategory
        }
    }
}