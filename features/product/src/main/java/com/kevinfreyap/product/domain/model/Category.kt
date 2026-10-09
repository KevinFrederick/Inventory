package com.kevinfreyap.product.domain.model

import com.kevinfreyap.domain.model.GroupId

@JvmInline
value class CategoryId(val value: String)

data class Category(
    val categoryId: CategoryId,
    val groupId: GroupId,
    val name: String,
    val description: String?,
    val createdAt: Long,
    val lastUpdated: Long
)
