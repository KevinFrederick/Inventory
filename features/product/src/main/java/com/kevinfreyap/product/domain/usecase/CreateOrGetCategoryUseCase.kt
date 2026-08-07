package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.Category
import com.kevinfreyap.product.domain.model.CategoryId
import com.kevinfreyap.product.domain.model.error.ProductCategoryError
import com.kevinfreyap.product.domain.repository.ICategoryRepository
import java.util.UUID
import javax.inject.Inject

class CreateOrGetCategoryUseCase @Inject constructor(
    private val repository: ICategoryRepository,
    private val validateProductCategory: ValidateProductCategoryUseCase
) {
    suspend operator fun invoke(rawCategory: String): Result<Category, ProductCategoryError> {
        val sanitizedCategory = when(
            val validationResult = validateProductCategory(rawCategory)
        ) {
            is Result.Success -> validationResult.data
            is Result.Error -> return validationResult
        }

        val existingCategory = repository.getCategoryByName(sanitizedCategory)

        return if (existingCategory != null) {
            Result.Success(existingCategory)
        } else {
            val newCategory = Category(
                categoryId = CategoryId("category-${UUID.randomUUID()}"),
                name = sanitizedCategory,
                description = null,
                createdAt = System.currentTimeMillis(),
                lastUpdated = System.currentTimeMillis()
            )
            repository.insertCategory(newCategory)
            Result.Success(newCategory)
        }
    }
}