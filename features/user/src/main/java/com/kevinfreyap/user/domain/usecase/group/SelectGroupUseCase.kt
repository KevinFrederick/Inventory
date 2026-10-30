package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.preferences.IDevicePreferences
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.domain.usecases.ObserveUserUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SelectGroupUseCase @Inject constructor(
    private val activeGroupSession: IActiveGroupSession,
    private val devicePreferences: IDevicePreferences,
    private val observeUser: ObserveUserUseCase
) {
    suspend operator fun invoke(
        groupId: GroupId
    ) {
        activeGroupSession.setActiveGroup(groupId.value)

        val userId = observeUser().first()?.userId
        if (userId != null) {
            devicePreferences.saveLastActiveGroup(
                userId = userId.value,
                groupId = groupId.value
            )
        }
    }
}