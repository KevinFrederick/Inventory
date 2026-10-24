package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.user.domain.model.GroupMember
import com.kevinfreyap.user.domain.repository.IGroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGroupMembersPreviewUseCase @Inject constructor(
    private val groupRepository: IGroupRepository
) {
    operator fun invoke(groupId: GroupId): Flow<List<GroupMember>> {
        return groupRepository.getMembersPreview(groupId)
    }
}