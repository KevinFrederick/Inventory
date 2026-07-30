package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.Category
import com.kevinfreyap.product.domain.repository.ICategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoryUseCase @Inject constructor(
    private val repository: ICategoryRepository
) {
    operator fun invoke(): Flow<List<Category>> {
        return repository.getAllCategory()
    }
}