package com.kevinfreyap.user.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.user.domain.usecase.group.ObserveGroupProfileSummaryUseCase
import com.kevinfreyap.user.domain.usecase.group.ObserveGroupWithStatsUseCase
import com.kevinfreyap.user.domain.usecase.user.ObserveUserUseCase
import com.kevinfreyap.user.presentation.mapper.toGroupItem
import com.kevinfreyap.user.presentation.state.ProfileScreenState
import com.kevinfreyap.user.presentation.state.UserDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    observeUser: ObserveUserUseCase,
    observeGroupProfileSummary: ObserveGroupProfileSummaryUseCase,
    private val observeGroupWithStats: ObserveGroupWithStatsUseCase
): ViewModel(){

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState<ProfileScreenState>> = combine(
        flow = observeUser(),
        flow2 = observeGroupProfileSummary(),
    ) { user, groupSummary ->
        Pair(user, groupSummary)
    }.flatMapLatest { (user, groupSummary) ->
        if (user == null) return@flatMapLatest flowOf(UiState.Empty)

        val userDetail = UserDetailState(
            name = user.name,
            email = user.email,
            profileUrl = user.profilePictureUrl,
            phoneNumber = user.phoneNumber,
            jobTitle = user.jobTitle
        )

        observeGroupWithStats(groupSummary.previewGroups).map { stats ->
            UiState.Success(
                ProfileScreenState(
                    userDetail = userDetail,
                    totalGroups = groupSummary.totalCount,
                    groups = stats.map { it.toGroupItem() }
                )
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )
}