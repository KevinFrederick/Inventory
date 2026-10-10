package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.model.Group
import com.kevinfreyap.user.domain.repository.IGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ObserveGroupsUseCase @Inject constructor(
    private val groupRepository: IGroupRepository
) {
    operator fun invoke(): Flow<List<Group>> {
        return groupRepository.getGroupsForUser()
            .onStart {
                groupRepository.refreshGroups()
            }
    }
}