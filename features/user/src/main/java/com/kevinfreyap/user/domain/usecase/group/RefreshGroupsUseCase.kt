package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.user.domain.repository.IGroupRepository
import javax.inject.Inject

class RefreshGroupsUseCase @Inject constructor(
    private val groupRepository: IGroupRepository
) {
    suspend operator fun invoke() {
        groupRepository.refreshGroups()
    }
}