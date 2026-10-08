package com.kevinfreyap.user.domain.validation.error

import com.kevinfreyap.domain.RootError
import com.kevinfreyap.network.error.NetworkError

data class GroupFormError(
    val groupNameError: GroupNameError? = null,
    val groupDescriptionError: GroupOptionalFieldError? = null,
    val groupAddressError: GroupOptionalFieldError? = null,
    val networkError: NetworkError? = null
): RootError
