package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.model.Group
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.user.domain.repository.IGroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGroupByIdUseCase @Inject constructor(
    private val groupRepository: IGroupRepository
) {
    operator fun invoke(groupId: GroupId): Flow<Group?> {
        return groupRepository.getGroupById(groupId)
    }
}