package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.product.domain.model.Location
import com.kevinfreyap.product.domain.repository.ILocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetAllLocationUseCase @Inject constructor(
    private val repository: ILocationRepository,
    private val activeGroupSession: IActiveGroupSession
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Location>> {
        return activeGroupSession.currentGroupId.flatMapLatest { groupId ->
            if (groupId.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                repository.getAllLocation(GroupId(groupId))
            }
        }
    }
}