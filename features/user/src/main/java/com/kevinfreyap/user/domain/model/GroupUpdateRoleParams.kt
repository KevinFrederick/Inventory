package com.kevinfreyap.user.domain.model

import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.model.UserId

data class GroupUpdateRoleParams(
    val newRole: AppRole
)
