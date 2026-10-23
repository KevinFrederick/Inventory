package com.kevinfreyap.user.domain.usecase.group

import com.kevinfreyap.user.domain.repository.IGroupRepository
import javax.inject.Inject

class OwnedGroupCountUseCase @Inject constructor(
    private val groupRepository: IGroupRepository
) {
    suspend operator fun invoke(): Int {
        return groupRepository.getOwnedGroupCount()
    }
}