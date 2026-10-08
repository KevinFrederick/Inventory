package com.kevinfreyap.user.domain.usecase.user

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.errorOrNull
import com.kevinfreyap.domain.util.getOrNull
import com.kevinfreyap.user.domain.model.UserUpdateParams
import com.kevinfreyap.user.domain.repository.IUserRepository
import com.kevinfreyap.user.domain.validation.ValidateUserJobTitle
import com.kevinfreyap.user.domain.validation.ValidateUserName
import com.kevinfreyap.user.domain.validation.ValidateUserPhoneNumber
import com.kevinfreyap.user.domain.validation.ValidateUserUrl
import com.kevinfreyap.user.domain.validation.error.UserFormError
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: IUserRepository,
    private val validateName: ValidateUserName,
    private val validateUrl: ValidateUserUrl,
    private val validatePhoneNumber: ValidateUserPhoneNumber,
    private val validateJobTitle: ValidateUserJobTitle,
) {
    suspend operator fun invoke(
        req: UserUpdateParams
    ): Result<Unit, UserFormError> {
        val nameResult = validateName(req.name)
        val avatarUrlResult = validateUrl(req.avatarUrl)
        val phoneNumberResult = validatePhoneNumber(req.phoneNumber)
        val jobTitleResult = validateJobTitle(req.jobTitle)

        if (
            nameResult is Result.Error ||
            avatarUrlResult is Result.Error ||
            phoneNumberResult is Result.Error ||
            jobTitleResult is Result.Error
        ) {
           return Result.Error(
               UserFormError(
                   nameError = nameResult.errorOrNull(),
                   urlError = avatarUrlResult.errorOrNull(),
                   phoneNumberError = phoneNumberResult.errorOrNull(),
                   jobTitleError = jobTitleResult.errorOrNull(),
               )
           )
        }

        val validatedParams = req.copy(
            name = requireNotNull(nameResult.getOrNull()),
            avatarUrl = avatarUrlResult.getOrNull(),
            phoneNumber = phoneNumberResult.getOrNull(),
            jobTitle = jobTitleResult.getOrNull(),
        )

        return when(
            val result = userRepository.updateUserProfile(validatedParams)
        ) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(
                UserFormError(
                    networkError = result.error
                )
            )
        }
    }
}