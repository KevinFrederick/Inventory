package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.Location
import com.kevinfreyap.product.domain.repository.ILocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllLocationUseCase @Inject constructor(
    private val repository: ILocationRepository
) {
    operator fun invoke(): Flow<List<Location>> {
        return repository.getAllLocation()
    }
}