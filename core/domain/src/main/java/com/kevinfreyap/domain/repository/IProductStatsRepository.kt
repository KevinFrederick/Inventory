package com.kevinfreyap.domain.repository

import com.kevinfreyap.domain.model.GroupId
import kotlinx.coroutines.flow.Flow

interface IProductStatsRepository {
    fun observeProductCount(groupIds: List<GroupId>): Flow<Map<String, Int>>
}