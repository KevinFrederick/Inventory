package com.kevinfreyap.user.presentation.screen.group_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.user.domain.usecase.group.ObserveGroupsUseCase
import com.kevinfreyap.user.domain.usecase.group.RefreshGroupsUseCase
import com.kevinfreyap.user.domain.usecase.group.RequestJoinGroupUseCase
import com.kevinfreyap.user.presentation.action.GroupListAction
import com.kevinfreyap.user.presentation.mapper.toGroupItem
import com.kevinfreyap.user.presentation.state.GroupListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.kevinfreyap.domain.Result
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.user.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import model.MembershipStatus

@HiltViewModel
class GroupListViewModel @Inject constructor(
    activeGroupSession: IActiveGroupSession,
    observeGroups: ObserveGroupsUseCase,
    private val refreshGroups: RefreshGroupsUseCase,
    private val requestJoinGroup: RequestJoinGroupUseCase
): ViewModel() {
    private val _uiEvent = Channel<UiEvent<Unit>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    private val _inviteCode = MutableStateFlow("")
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<UiState<GroupListScreenState>> = combine(
        flow = observeGroups(),
        flow2 = activeGroupSession.currentGroupId,
        flow3 = _inviteCode,
        flow4 = _isLoading,
        flow5 = _errorMessage,
    ) { groups, activeGroupId, inviteCode, isLoading, errorMessage ->
        if (isLoading) return@combine UiState.Loading

        UiState.Success(
            GroupListScreenState(
                activeGroups = groups
                    .map { it.toGroupItem(activeGroupId) }
                    .filter { it.status == MembershipStatus.ACTIVE },
                pendingGroups = groups
                    .map { it.toGroupItem(activeGroupId) }
                    .filter { it.status == MembershipStatus.PENDING },
                inviteCode = inviteCode,
                errorMessage = errorMessage
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        onAction(GroupListAction.OnRefresh)
    }

    fun onAction(action: GroupListAction) {
        when (action) {
            is GroupListAction.OnInviteCodeChanged -> {
                _errorMessage.value = null
                _inviteCode.value = action.code.uppercase()
            }
            is GroupListAction.OnJoinGroupClick -> joinGroup()
            is GroupListAction.OnRefresh -> refreshData()
        }
    }

    private fun joinGroup() {
        val code = _inviteCode.value.trim()
        if (code.isBlank()) {
            _errorMessage.value = "Invite code is required"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            val result = requestJoinGroup(code)
            when (result) {
                is Result.Success -> {
                    _inviteCode.value = ""
                    _uiEvent.send(UiEvent.ShowToast(R.string.success_request_sent))
                    refreshGroups()
                }
                is Result.Error -> {
                    _errorMessage.value = "Failed to join group. Check your code or internet."
                }
            }

            _isLoading.value = false
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            refreshGroups()
            _isLoading.value = false
        }
    }
}