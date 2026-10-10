package com.kevinfreyap.user.data.mapper

import com.kevinfreyap.database.entity.UserEntity
import com.kevinfreyap.domain.model.User
import com.kevinfreyap.domain.model.UserId
import com.kevinfreyap.network.dto.response.UserResponse

fun UserEntity.toDomain(): User =
    User(
        userId = UserId(userId),
        email = email,
        name = name,
        profilePictureUrl = this.profilePictureUrl?.let { "$it?v=${this.lastUpdated}" },
        phoneNumber = phoneNumber,
        jobTitle = jobTitle,
        locale = locale,
        timeZone = timeZone,
        createdAt = createdAt,
        lastUpdated = lastUpdated
    )

fun UserResponse.toEntity(): UserEntity =
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