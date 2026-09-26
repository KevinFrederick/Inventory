package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.repository.ISyncRepository
import javax.inject.Inject

class ObserveRealTimeUpdateUseCase @Inject constructor(
    private val syncRepository: ISyncRepository
) {
    suspend operator fun invoke() {
        syncRepository.observeRealTimeUpdates()
    }
}