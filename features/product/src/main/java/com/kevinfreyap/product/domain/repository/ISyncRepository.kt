package com.kevinfreyap.product.domain.repository

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.model.GroupId

interface ISyncRepository {
    suspend fun sync(groupId: GroupId): Result<Unit, NetworkError>
    suspend fun observeRealTimeUpdates(groupId: GroupId)
}