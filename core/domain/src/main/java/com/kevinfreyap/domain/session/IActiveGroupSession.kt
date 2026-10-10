package com.kevinfreyap.domain.session

import kotlinx.coroutines.flow.Flow

interface IActiveGroupSession {
    val currentGroupId: Flow<String?>
    suspend fun setActiveGroup(groupId: String)
    suspend fun clear()
}