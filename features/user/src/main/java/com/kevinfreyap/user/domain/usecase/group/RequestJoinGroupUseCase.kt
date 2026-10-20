package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.Result
import com.kevinfreyap.user.domain.repository.IGroupRepository
import com.kevinfreyap.user.domain.validation.error.CodeError
import com.kevinfreyap.user.domain.validation.error.InviteCodeError
import javax.inject.Inject

class RequestJoinGroupUseCase @Inject constructor(
    private val groupRepository: IGroupRepository
) {
    suspend operator fun invoke(inviteCode: String): Result<Unit, InviteCodeError> {
        if (inviteCode.isBlank()) {
            return Result.Error(InviteCodeError(fieldError = CodeError.EMPTY))
        }

        return when(
            val result = groupRepository.requestJoinGroup(inviteCode.trim().uppercase())
        ) {
            is Result.Success -> {
                Result.Success(Unit)
            }
            is Result.Error -> {
                Result.Error(
                    InviteCodeError(
                        networkError = result.error
                    )
                )
            }
        }
    }
}