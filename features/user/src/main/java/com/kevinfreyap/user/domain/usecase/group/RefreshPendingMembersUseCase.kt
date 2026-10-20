package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.user.domain.repository.IGroupRepository
import javax.inject.Inject

class RefreshPendingMembersUseCase @Inject constructor(
    private val groupRepository: IGroupRepository
) {
    suspend operator fun invoke(groupId: GroupId) {
        groupRepository.refreshPendingMembers(groupId)
    }
}