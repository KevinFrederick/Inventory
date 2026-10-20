package com.kevinfreyap.user.presentation.screen.group_members

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.model.UserId
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.user.domain.usecase.group.ApproveMemberUseCase
import com.kevinfreyap.user.domain.usecase.group.CreateInviteCodeUseCase
import com.kevinfreyap.user.domain.usecase.group.ObserveGroupByIdUseCase
import com.kevinfreyap.user.domain.usecase.group.ObserveGroupMembersUseCase
import com.kevinfreyap.user.domain.usecase.group.ObservePendingMembersUseCase
import com.kevinfreyap.user.domain.usecase.group.RefreshGroupMembersUseCase
import com.kevinfreyap.user.domain.usecase.group.RefreshPendingMembersUseCase
import com.kevinfreyap.user.domain.usecase.group.RejectMemberUseCase
import com.kevinfreyap.user.presentation.action.MembersAction
import com.kevinfreyap.user.presentation.mapper.toMemberItem
import com.kevinfreyap.user.presentation.mapper.toPendingMember
import com.kevinfreyap.user.presentation.navigation.UserScreens
import com.kevinfreyap.user.presentation.state.GroupMembersScreenState
import com.kevinfreyap.user.presentation.util.AssignableRoles.getAssignableRoles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupMembersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeGroupById: ObserveGroupByIdUseCase,
    observePendingMembers: ObservePendingMembersUseCase,
    observeGroupMembers: ObserveGroupMembersUseCase,
    private val generateInviteCode: CreateInviteCodeUseCase,
    private val approveMember: ApproveMemberUseCase,
    private val rejectMember: RejectMemberUseCase,
    private val refreshGroupMembers: RefreshGroupMembersUseCase,
    private val refreshPendingMembers: RefreshPendingMembersUseCase
): ViewModel() {
    private val route = savedStateHandle.toRoute<UserScreens.GroupDetailScreen>()
    private val groupId = GroupId(route.groupId)

    private val _isLoading = MutableStateFlow(false)
    private val _generatedInviteCode = MutableStateFlow<String?>(null)

    val uiState: StateFlow<UiState<GroupMembersScreenState>> = combine(
        flow = observePendingMembers(groupId),
        flow2 = observeGroupMembers(groupId),
        flow3 = observeGroupById(groupId),
        flow4 = _generatedInviteCode,
        flow5 = _isLoading
    ) { pendingMembers, groupMembers, currentGroup, inviteCode, isLoading  ->
        if (isLoading) return@combine UiState.Loading
        if (currentGroup == null) return@combine UiState.Empty

        UiState.Success(
            GroupMembersScreenState(
                activeMembers = groupMembers.map { it.toMemberItem() },
                pendingMembers = pendingMembers.map { it.toPendingMember() },
                loggedInUserRole = currentGroup.group.currentUserRole,
                assignableRoles = getAssignableRoles(currentGroup.group.currentUserRole),
                generatedInviteCode = inviteCode
            )
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            refreshGroupMembers(groupId)
            refreshPendingMembers(groupId)
            _isLoading.value = false
        }
    }

    fun onAction(action: MembersAction) {
        when(action) {
            is MembersAction.OnGenerateInviteCode -> generateInviteCode()
            is MembersAction.OnApproveMember -> approveMember(action.memberId, action.role)
            is MembersAction.OnRejectMember -> rejectMember(action.memberId)
            is MembersAction.OnDismissShareSheet -> _generatedInviteCode.value = null
        }
    }

    private fun generateInviteCode() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = generateInviteCode(groupId, 7)
            if (result is Result.Success) {
                _generatedInviteCode.value = result.data
            }
            _isLoading.value = false
        }
    }

    private fun approveMember(memberId: String, role: AppRole) {
        viewModelScope.launch {
            _isLoading.value = true
            approveMember(groupId, UserId(memberId), role)
            _isLoading.value = false
        }
    }

    private fun rejectMember(memberId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            rejectMember(groupId, UserId(memberId))
            _isLoading.value = false
        }
    }
}