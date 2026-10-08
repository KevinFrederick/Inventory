package com.kevinfreyap.user.domain.validation.error

import com.kevinfreyap.domain.RootError
import com.kevinfreyap.network.error.NetworkError

data class UserFormError(
    val nameError: UserNameError? = null,
    val urlError: UserOptionalFieldError? = null,
    val phoneNumberError: UserPhoneNumberError? = null,
    val jobTitleError: UserOptionalFieldError? = null,
    val networkError: NetworkError? = null
): RootError
