package com.kevinfreyap.product.domain.repository

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.product.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    fun getAllLocation(groupId: GroupId): Flow<List<Location>>

    suspend fun getLocationByName(name: String, groupId: GroupId): Location?

    suspend fun insertLocation(location: Location)
}