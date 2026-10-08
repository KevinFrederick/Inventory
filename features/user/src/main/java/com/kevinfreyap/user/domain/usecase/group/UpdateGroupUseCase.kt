package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.util.errorOrNull
import com.kevinfreyap.domain.util.getOrNull
import com.kevinfreyap.user.domain.model.GroupParams
import com.kevinfreyap.user.domain.repository.IGroupRepository
import com.kevinfreyap.user.domain.validation.ValidateGroupAddress
import com.kevinfreyap.user.domain.validation.ValidateGroupDescription
import com.kevinfreyap.user.domain.validation.ValidateGroupName
import com.kevinfreyap.user.domain.validation.error.GroupFormError
import javax.inject.Inject

class UpdateGroupUseCase @Inject constructor(
    private val groupRepository: IGroupRepository,
    private val validateGroupName: ValidateGroupName,
    private val validateGroupDescription: ValidateGroupDescription,
    private val validateGroupAddress: ValidateGroupAddress
) {
    suspend operator fun invoke(
        groupId: GroupId,
        newGroup: GroupParams
    ): Result<Unit, GroupFormError> {
        val nameResult = validateGroupName(newGroup.name)
        val descriptionResult = validateGroupDescription(newGroup.description)
        val addressResult = validateGroupAddress(newGroup.address)

        if (
            nameResult is Result.Error ||
            descriptionResult is Result.Error ||
            addressResult is Result.Error
        ) {
            return Result.Error(
                GroupFormError(
                    groupNameError = nameResult.errorOrNull(),
                    groupDescriptionError = descriptionResult.errorOrNull(),
                    groupAddressError = addressResult.errorOrNull(),
                )
            )
        }

        val validatedGroup = GroupParams(
            name = requireNotNull(nameResult.getOrNull()),
            description = descriptionResult.getOrNull(),
            address = addressResult.getOrNull()
        )

        return when(
            val result = groupRepository.updateGroup(groupId, validatedGroup)
        ) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(
                GroupFormError(
                    networkError = result.error
                )
            )
        }
    }
}