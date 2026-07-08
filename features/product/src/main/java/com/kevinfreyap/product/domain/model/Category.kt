package com.kevinfreyap.product.domain.model

data class Category(
    val categoryId: String,
    val name: String,
    val description: String?,
    val createdAt: Long,
    val lastUpdated: Long
)
