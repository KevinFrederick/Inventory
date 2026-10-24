package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.repository.IProductStatsRepository
import com.kevinfreyap.user.domain.model.GroupProfileSummary
import com.kevinfreyap.user.domain.repository.IGroupRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveGroupProfileSummaryUseCase @Inject constructor(
    private val groupRepository: IGroupRepository,
    private val productRepository: IProductStatsRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<GroupProfileSummary> {
        return combine(
            groupRepository.getTotalGroupCount(),
            groupRepository.getPreviewGroups()
        ) {totalCount, groups ->
            Pair(totalCount, groups)
        }.flatMapLatest { (totalCount, groups) ->

            if (groups.isEmpty()) {
                return@flatMapLatest flowOf(GroupProfileSummary(totalCount, emptyList()))
            }

            val groupIds = groups.map { it.groupId }

            combine(
                groupRepository.getMemberCount(groupIds),
                productRepository.observeProductCount(groupIds)
            ) { memberMap, productMap ->

                val updatedGroups = groups.map { group ->
                    val groupIdString = group.groupId.value

                    group.copy(
                        totalMembers = memberMap[groupIdString] ?: group.totalMembers,
                        totalProducts = productMap[groupIdString] ?: group.totalProducts
                    )
                }

                GroupProfileSummary(
                    totalCount = totalCount,
                    previewGroups = updatedGroups
                )
            }
        }
    }
}