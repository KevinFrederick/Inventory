package com.kevinfreyap.user.domain.validation.error

import com.kevinfreyap.domain.error.RootError
import com.kevinfreyap.domain.error.ImageError
import com.kevinfreyap.domain.error.NetworkError

data class UserFormError(
    val nameError: UserNameError? = null,
    val urlError: ImageError? = null,
    val phoneNumberError: UserPhoneNumberError? = null,
    val jobTitleError: UserOptionalFieldError? = null,
    val networkError: NetworkError? = null
): RootError
