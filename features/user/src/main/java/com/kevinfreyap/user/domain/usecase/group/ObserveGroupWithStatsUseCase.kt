package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.model.Group
import com.kevinfreyap.domain.repository.IProductStatsRepository
import com.kevinfreyap.user.domain.model.GroupWithStats
import com.kevinfreyap.user.domain.repository.IGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.collections.emptyList

class ObserveGroupWithStatsUseCase @Inject constructor(
    private val groupRepository: IGroupRepository,
    private val productStatsRepository: IProductStatsRepository
) {
    operator fun invoke(
        groups: List<Group>
    ): Flow<List<GroupWithStats>> {
        if (groups.isEmpty()) return flowOf(emptyList())

        val groupIds = groups.map { it.groupId }

        return combine(
            groupRepository.getMemberCount(groupIds),
            productStatsRepository.observeProductCount(groupIds)
        ) {memberMap, productMap ->
            groups.map { group ->
                GroupWithStats(
                    group = group,
                    totalMembers = memberMap[group.groupId.value] ?: 0,
                    totalProducts = productMap[group.groupId.value] ?: 0
                )
            }
        }
    }
}