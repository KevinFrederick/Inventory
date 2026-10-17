package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.domain.model.Group
import com.kevinfreyap.domain.repository.IUserGroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGroupsUseCase @Inject constructor(
    private val groupRepository: IUserGroupRepository
) {
    operator fun invoke(): Flow<List<Group>> {
        return groupRepository.getGroupsForUser()
    }
}