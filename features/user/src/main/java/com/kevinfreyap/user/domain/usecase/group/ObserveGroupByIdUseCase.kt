package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.repository.IProductStatsRepository
import com.kevinfreyap.user.domain.model.GroupWithStats
import com.kevinfreyap.user.domain.repository.IGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveGroupByIdUseCase @Inject constructor(
    private val groupRepository: IGroupRepository,
    private val productStatsRepository: IProductStatsRepository
) {
    operator fun invoke(groupId: GroupId): Flow<GroupWithStats?> {
        val groupIds = listOf(groupId)
        return combine(
            flow = groupRepository.getGroupById(groupId),
            flow2 = groupRepository.getMemberCount(groupIds),
            flow3 = productStatsRepository.observeProductCount(groupIds)
        ) { group, members, productMap ->
            if (group == null) {
                null
            } else {
                GroupWithStats(
                    group = group,
                    totalMembers = members[group.groupId.value] ?: 0,
                    totalProducts = productMap[group.groupId.value] ?: 0
                )
            }
        }
    }
}