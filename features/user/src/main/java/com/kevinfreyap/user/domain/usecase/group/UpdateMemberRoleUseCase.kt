package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.model.UserId
import com.kevinfreyap.network.error.NetworkError
import com.kevinfreyap.user.domain.model.GroupUpdateRoleParams
import com.kevinfreyap.user.domain.repository.IGroupRepository
import javax.inject.Inject

class UpdateMemberRoleUseCase @Inject constructor(
    private val groupRepository: IGroupRepository
) {
    suspend operator fun invoke(
        groupId: GroupId,
        targetUserId: UserId,
        req: GroupUpdateRoleParams
    ): Result<Unit, NetworkError> {
        return groupRepository.updateMemberRole(
            groupId = groupId,
            targetUserId = targetUserId,
            req = req
        )
    }
}