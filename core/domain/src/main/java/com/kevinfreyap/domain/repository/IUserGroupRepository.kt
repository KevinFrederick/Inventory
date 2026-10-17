package com.kevinfreyap.domain.repository

import com.kevinfreyap.domain.model.Group
import kotlinx.coroutines.flow.Flow

interface IUserGroupRepository {
    fun getGroupsForUser(): Flow<List<Group>>
}