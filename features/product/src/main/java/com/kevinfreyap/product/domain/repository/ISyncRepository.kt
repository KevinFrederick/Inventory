package com.kevinfreyap.product.domain.repository

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.NetworkError

interface ISyncRepository {
    suspend fun sync(): Result<Unit, NetworkError>
    suspend fun observeRealTimeUpdates()
}