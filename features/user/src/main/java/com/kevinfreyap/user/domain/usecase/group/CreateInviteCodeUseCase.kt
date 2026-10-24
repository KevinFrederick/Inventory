package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.user.domain.repository.IGroupRepository
import javax.inject.Inject

class CreateInviteCodeUseCase @Inject constructor(
    private val groupRepository: IGroupRepository,
) {
    suspend operator fun invoke(groupId: GroupId, validityInDays: Int): Result<String, NetworkError> {
        return groupRepository.createInviteCode(groupId, validityInDays)
    }
}