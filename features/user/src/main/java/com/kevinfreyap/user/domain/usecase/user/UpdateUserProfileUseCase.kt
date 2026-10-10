package com.kevinfreyap.user.domain.usecase.user

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.manager.IImageManager
import com.kevinfreyap.domain.util.errorOrNull
import com.kevinfreyap.domain.util.getOrNull
import com.kevinfreyap.domain.validation.ValidateImageUseCase
import com.kevinfreyap.user.domain.model.UserUpdateParams
import com.kevinfreyap.user.domain.repository.IUserRepository
import com.kevinfreyap.user.domain.validation.ValidateUserJobTitle
import com.kevinfreyap.user.domain.validation.ValidateUserName
import com.kevinfreyap.user.domain.validation.ValidateUserPhoneNumber
import com.kevinfreyap.user.domain.validation.error.UserFormError
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: IUserRepository,
    private val imageManager: IImageManager,
    private val validateName: ValidateUserName,
    private val validateImageUrl: ValidateImageUseCase,
    private val validatePhoneNumber: ValidateUserPhoneNumber,
    private val validateJobTitle: ValidateUserJobTitle,
) {
    suspend operator fun invoke(
        req: UserUpdateParams
    ): Result<Unit, UserFormError> {
        val nameResult = validateName(req.name)
        val imageUrlResult = validateImageUrl(req.profilePictureUrl)
        val phoneNumberResult = validatePhoneNumber(req.phoneNumber)
        val jobTitleResult = validateJobTitle(req.jobTitle)

        if (
            nameResult is Result.Error ||
            imageUrlResult is Result.Error ||
            phoneNumberResult is Result.Error ||
            jobTitleResult is Result.Error
        ) {
           return Result.Error(
               UserFormError(
                   nameError = nameResult.errorOrNull(),
                   urlError = imageUrlResult.errorOrNull(),
                   phoneNumberError = phoneNumberResult.errorOrNull(),
                   jobTitleError = jobTitleResult.errorOrNull(),
               )
           )
        }

        val currentUrl = imageUrlResult.getOrNull()

        val permanentPath = if (currentUrl != null && currentUrl.startsWith("content://")) {
            imageManager.saveImageToInternalStorage(currentUrl)
        } else {
            currentUrl
        }

        var finalPath = permanentPath

        if (finalPath == null) {
            when(
                val deleteResult = userRepository.deleteProfilePicture()
            ) {
                is Result.Success -> {  }
                is Result.Error -> return Result.Error(UserFormError(networkError = deleteResult.error))
            }
        } else if (finalPath.startsWith("/")) {
            when(
                val uploadResult = userRepository.uploadProfilePicture(finalPath)
            ) {
                is Result.Success -> {
                    finalPath = uploadResult.data
                }
                is Result.Error -> {
                    return Result.Error(
                        UserFormError(
                            networkError = uploadResult.error
                        )
                    )
                }
            }
        }

        val validatedParams = req.copy(
            name = requireNotNull(nameResult.getOrNull()),
            profilePictureUrl = finalPath,
            phoneNumber = phoneNumberResult.getOrNull(),
            jobTitle = jobTitleResult.getOrNull(),
        )

        return when(
            val result = userRepository.updateUserProfile(validatedParams)
        ) {
            is Result.Success -> {
                Result.Success(Unit)
            }
            is Result.Error -> {
                Result.Error(
                    UserFormError(
                        networkError = result.error
                    )
                )
            }
        }
    }
}