package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.user.domain.model.GroupProfileSummary
import com.kevinfreyap.user.domain.repository.IGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ObserveGroupProfileSummaryUseCase @Inject constructor(
    private val groupRepository: IGroupRepository
) {
    operator fun invoke(): Flow<GroupProfileSummary> {
        return combine(
            groupRepository.getTotalGroupCount(),
            groupRepository.getPreviewGroups()
        ) {totalCount, groups ->
            GroupProfileSummary(
                totalCount = totalCount,
                previewGroups = groups
            )
        }.onStart {
            groupRepository.refreshGroups()
        }
    }
}