package com.kevinfreyap.product.data.repository

import com.kevinfreyap.database.dao.LocationDao
import com.kevinfreyap.database.model.SyncState
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.product.data.mapper.toDomain
import com.kevinfreyap.product.data.mapper.toEntity
import com.kevinfreyap.product.domain.model.Location
import com.kevinfreyap.product.domain.repository.ILocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationDao: LocationDao
): ILocationRepository {
    override fun getAllLocation(groupId: GroupId): Flow<List<Location>> {
        return locationDao.getAllLocation(groupId.value).map { locationEntities ->
            locationEntities.toDomain()
        }
    }

    override suspend fun getLocationByName(name: String, groupId: GroupId): Location? {
        return locationDao.getLocationByName(
            name = name,
            groupId = groupId.value
        )?.toDomain()
    }

    override suspend fun insertLocation(location: Location) {
        locationDao.insertLocation(location.toEntity(SyncState.CREATED))
    }
}