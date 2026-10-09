package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.product.domain.repository.ISyncRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveRealTimeUpdateUseCase @Inject constructor(
    private val syncRepository: ISyncRepository,
    private val activeGroupSession: IActiveGroupSession
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke() {
        activeGroupSession.currentGroupId
            .flatMapLatest { session ->
                if (session.isNullOrBlank()) {
                    emptyFlow()
                } else {
                    flow<Unit> {
                        syncRepository.observeRealTimeUpdates(GroupId(session))
                    }
                }
            }
            .collect()
    }
}