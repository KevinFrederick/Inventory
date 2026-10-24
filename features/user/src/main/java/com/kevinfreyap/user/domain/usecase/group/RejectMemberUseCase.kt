package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.model.UserId
import com.kevinfreyap.user.domain.repository.IGroupRepository
import javax.inject.Inject

class RejectMemberUseCase @Inject constructor(
    val groupRepository: IGroupRepository
) {
    suspend operator fun invoke(
        groupId: GroupId,
        memberId: UserId
    ): Result<Unit, NetworkError> {
        return groupRepository.rejectMember(
            groupId = groupId,
            memberId = memberId
        )
    }
}