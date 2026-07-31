package com.kevinfreyap.product.domain.repository

import com.kevinfreyap.product.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    fun getAllLocation(): Flow<List<Location>>
}