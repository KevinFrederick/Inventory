package com.kevinfreyap.user.presentation.util

import com.kevinfreyap.domain.model.AppRole

object AssignableRoles {
    fun getAssignableRoles(loggedInUserRole: AppRole): List<AppRole> {
        return when (loggedInUserRole) {
            AppRole.OWNER -> listOf(AppRole.ADMIN, AppRole.MEMBER)
            AppRole.ADMIN -> listOf(AppRole.MEMBER)
            AppRole.MEMBER -> emptyList()
        }
    }
}