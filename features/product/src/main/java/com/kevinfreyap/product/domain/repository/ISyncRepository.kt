package com.kevinfreyap.product.domain.repository

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.NetworkError

interface ISyncRepository {
    suspend fun sync(): Result<Unit, NetworkError>
    suspend fun observeRealTimeUpdates()
}