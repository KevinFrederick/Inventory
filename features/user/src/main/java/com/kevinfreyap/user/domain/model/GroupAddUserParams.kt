package com.kevinfreyap.user.domain.model

import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.domain.model.GroupId

data class GroupAddUserParams(
    val targetEmail: String,
    val assignRole: AppRole
)
