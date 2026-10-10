package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.validation.ValidateEmailUseCase
import com.kevinfreyap.user.domain.model.GroupAddUserParams
import com.kevinfreyap.user.domain.repository.IGroupRepository
import com.kevinfreyap.user.domain.validation.error.MemberFormError
import javax.inject.Inject
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.errorOrNull
import com.kevinfreyap.domain.util.getOrNull

class AddMemberUseCase @Inject constructor(
    private val groupRepository: IGroupRepository,
    private val validateEmail: ValidateEmailUseCase
) {
    suspend operator fun invoke(
        groupId: GroupId,
        req: GroupAddUserParams
    ): Result<Unit, MemberFormError> {
        val emailResult = validateEmail(req.targetEmail)

        if (emailResult is Result.Error) {
            return Result.Error(
                MemberFormError(
                    emailError = emailResult.errorOrNull()
                )
            )
        }

        val validatedReq = req.copy(
            targetEmail = requireNotNull(emailResult.getOrNull())
        )

        return when (
            val result = groupRepository.addMemberToGroup(groupId, validatedReq)
        ) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(
                MemberFormError(
                    networkError = result.error
                )
            )
        }
    }
}