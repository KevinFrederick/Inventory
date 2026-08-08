package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.Category
import com.kevinfreyap.product.domain.model.CategoryId
import com.kevinfreyap.product.domain.repository.ICategoryRepository
import java.util.UUID
import javax.inject.Inject

class CreateOrGetCategoryUseCase @Inject constructor(
    private val repository: ICategoryRepository,
) {
    suspend operator fun invoke(validCategoryName: String): Category {
        val existingCategory = repository.getCategoryByName(validCategoryName)

        return if (existingCategory != null) {
            existingCategory
        } else {
            val newCategory = Category(
                categoryId = CategoryId("category-${UUID.randomUUID()}"),
                name = validCategoryName,
                description = null,
                createdAt = System.currentTimeMillis(),
                lastUpdated = System.currentTimeMillis()
            )
            repository.insertCategory(newCategory)
            newCategory
        }
    }
}