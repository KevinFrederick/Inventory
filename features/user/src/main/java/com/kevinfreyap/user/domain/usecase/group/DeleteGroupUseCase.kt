package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.user.domain.repository.IGroupRepository
import javax.inject.Inject

class DeleteGroupUseCase @Inject constructor(
    private val groupRepository: IGroupRepository
) {
    suspend operator fun invoke(groupId: GroupId): Result<Unit, NetworkError> {
        return groupRepository.deleteGroup(groupId)
    }
}