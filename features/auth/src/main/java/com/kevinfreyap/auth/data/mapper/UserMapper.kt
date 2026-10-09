package com.kevinfreyap.auth.data.mapper

import com.kevinfreyap.database.entity.UserEntity
import com.kevinfreyap.network.dto.response.UserResponse

internal fun UserResponse.toEntity(): UserEntity =
    UserEntity(
        userId = userId,
        email = email,
        name = name,
        profilePictureUrl = avatarUrl,
        phoneNumber = phoneNumber,
        jobTitle = jobTitle,
        locale = locale,
        timeZone = timeZone,
        createdAt = createdAt,
        lastUpdated = lastUpdated
    )