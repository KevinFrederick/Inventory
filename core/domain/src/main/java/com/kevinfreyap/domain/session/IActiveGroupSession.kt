package com.kevinfreyap.domain.session

import com.kevinfreyap.domain.model.GroupId
import kotlinx.coroutines.flow.Flow

interface IActiveGroupSession {
    val currentGroupId: Flow<String?>
    suspend fun setActiveGroup(groupId: GroupId)
    suspend fun clear()
}