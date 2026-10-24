package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.model.UserId
import com.kevinfreyap.user.domain.repository.IGroupRepository
import javax.inject.Inject

class ApproveMemberUseCase @Inject constructor(
    private val groupRepository: IGroupRepository
) {
    suspend operator fun invoke(
        groupId: GroupId,
        memberId: UserId,
        role: AppRole
    ): Result<Unit, NetworkError> {
        return groupRepository.approveMember(
            groupId = groupId,
            memberId = memberId,
            role = role
        )
    }
}