package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.model.Group
import com.kevinfreyap.domain.repository.IUserGroupRepository
import com.kevinfreyap.domain.session.IActiveGroupSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveActiveGroupUseCase @Inject constructor(
    private val activeGroupSession: IActiveGroupSession,
    private val userGroupRepository: IUserGroupRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Group?> {
        return activeGroupSession.currentGroupId.flatMapLatest { activeGroupId ->
            if (activeGroupId == null) {
                flowOf(null)
            } else {
                userGroupRepository.getGroupsForUser().map { groups ->
                    groups.find { it.groupId.value == activeGroupId }
                }
            }
        }
    }
}