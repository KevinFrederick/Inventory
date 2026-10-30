package com.kevinfreyap.user.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.user.R
import com.kevinfreyap.user.domain.usecase.auth.LogoutUseCase
import com.kevinfreyap.user.domain.usecase.group.ObserveGroupProfileSummaryUseCase
import com.kevinfreyap.user.domain.usecase.group.RefreshGroupMembersUseCase
import com.kevinfreyap.user.domain.usecase.group.RefreshGroupsUseCase
import com.kevinfreyap.domain.usecases.ObserveUserUseCase
import com.kevinfreyap.user.domain.usecase.user.RefreshUserUseCase
import com.kevinfreyap.user.presentation.mapper.toGroupItem
import com.kevinfreyap.user.presentation.navigation.ProfileNavigation
import com.kevinfreyap.user.presentation.state.ProfileScreenState
import com.kevinfreyap.user.presentation.state.UserDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    observeUser: ObserveUserUseCase,
    activeGroupSession: IActiveGroupSession,
    private val logoutUser: LogoutUseCase,
    private val observeGroupProfileSummary: ObserveGroupProfileSummaryUseCase,
    private val refreshUser: RefreshUserUseCase,
    private val refreshGroups: RefreshGroupsUseCase,
    private val refreshGroupMembers: RefreshGroupMembersUseCase,
): ViewModel(){

    private val _uiEvent = Channel<UiEvent<ProfileNavigation>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val retryTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState<ProfileScreenState>> = retryTrigger
        .flatMapLatest {
            combine(
                flow = observeUser().distinctUntilChanged(),
                flow2 = observeGroupProfileSummary().distinctUntilChanged(),
                flow3 = activeGroupSession.currentGroupId,
            ) { user, groups, activeId ->
                if (user == null) return@combine UiState.Error("Failed to load profile")

                val (totalGroups, stats) = groups

                val userDetail = UserDetailState(
                    name = user.name,
                    email = user.email,
                    profileUrl = user.profilePictureUrl,
                    phoneNumber = user.phoneNumber,
                    jobTitle = user.jobTitle
                )

                UiState.Success(
                    ProfileScreenState(
                        userDetail = userDetail,
                        totalGroups = totalGroups,
                        groups = stats
                            .map { it.toGroupItem(activeId) }
                    )
                )
            }
        }
        .onStart { emit(UiState.Loading) }
        .catch { exception ->
            emit(UiState.Error(exception.message ?: "Failed to load profile"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    init {
        triggerBackgroundSync()
    }

    private fun triggerBackgroundSync()  {
        viewModelScope.launch {
            launch { refreshUser() }
            launch {
                refreshGroups()

                val currentGroups = observeGroupProfileSummary().first()

                currentGroups.previewGroups.forEach { group ->
                    launch { refreshGroupMembers(group.groupId) }
                }
            }
        }
    }

    fun onRetryClicked() {
        retryTrigger.value += 1
        triggerBackgroundSync()
    }

    fun logout() {
        viewModelScope.launch {
            when(
                val result = logoutUser()
            ) {
                is Result.Success -> {
                    _uiEvent.send(UiEvent.ShowToast(R.string.success_logout))
                    _uiEvent.send(UiEvent.Navigate(ProfileNavigation.Onboard))
                }
                is Result.Error -> {
                    when (
                        val error = result.error
                    ) {
                        NetworkError.Local.NO_INTERNET -> {
                            _uiEvent.send(UiEvent.ShowToast(R.string.error_no_internet))
                        }
                        NetworkError.Local.MISSING_FILE -> {
                            _uiEvent.send(UiEvent.ShowToast(R.string.error_missing_file))
                        }
                        NetworkError.Local.UNKNOWN -> {
                            _uiEvent.send(UiEvent.ShowToast(R.string.error_unknown))
                        }
                        is NetworkError.Server -> {
                            _uiEvent.send(UiEvent.ShowToast(error.message))
                        }
                    }
                }
            }
        }
    }
}