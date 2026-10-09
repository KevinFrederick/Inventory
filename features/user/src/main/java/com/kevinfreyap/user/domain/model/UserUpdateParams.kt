package com.kevinfreyap.user.domain.model

data class UserUpdateParams (
    val name: String,
    val profilePictureUrl: String?,
    val phoneNumber: String?,
    val jobTitle: String?,
    val locale: String?,
    val timeZone: String?,
)