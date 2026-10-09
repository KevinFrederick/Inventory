package com.kevinfreyap.user.presentation.mapper

import com.kevinfreyap.domain.model.User
import com.kevinfreyap.user.presentation.state.UserDetailState

fun User.toUserDetail(): UserDetailState =
    UserDetailState(
        name = name,
        email = email,
        profileUrl = profilePictureUrl,
        phoneNumber = phoneNumber,
        jobTitle = jobTitle
    )