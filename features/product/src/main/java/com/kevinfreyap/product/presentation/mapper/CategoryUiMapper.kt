package com.kevinfreyap.product.presentation.mapper

import com.kevinfreyap.product.domain.model.Category
import com.kevinfreyap.product.presentation.model.CategoryUi

fun Category.toUi(): CategoryUi {
    return CategoryUi(
        id = this.categoryId.value,
        name = this.name
    )
}

fun List<Category>.toUi(): List<CategoryUi> {
    return this.map { category ->
        category.toUi()
    }
}