package com.kevinfreyap.user.presentation.screen.group_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.util.toFullDateString
import com.kevinfreyap.user.R
import com.kevinfreyap.user.domain.usecase.group.DeleteGroupUseCase
import com.kevinfreyap.user.domain.usecase.group.ObserveGroupByIdUseCase
import com.kevinfreyap.user.domain.usecase.group.ObserveGroupMembersPreviewUseCase
import com.kevinfreyap.user.domain.usecase.group.OwnedGroupCountUseCase
import com.kevinfreyap.user.domain.usecase.group.RefreshGroupMembersUseCase
import com.kevinfreyap.user.domain.usecase.group.SelectGroupUseCase
import com.kevinfreyap.domain.usecases.ObserveUserUseCase
import com.kevinfreyap.user.presentation.mapper.toMemberItem
import com.kevinfreyap.user.presentation.navigation.GroupDetailNavigation
import com.kevinfreyap.user.presentation.navigation.UserScreens
import com.kevinfreyap.user.presentation.state.GroupDetailScreenState
import com.kevinfreyap.user.presentation.state.GroupDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    observeGroupById: ObserveGroupByIdUseCase,
    observeGroupMembersPreview: ObserveGroupMembersPreviewUseCase,
    observeUser: ObserveUserUseCase,
    savedStateHandle: SavedStateHandle,
    activeGroupSession: IActiveGroupSession,
    private val selectGroup: SelectGroupUseCase,
    private val deleteGroup: DeleteGroupUseCase,
    private val refreshGroupMembers: RefreshGroupMembersUseCase,
    private val ownedGroupCount: OwnedGroupCountUseCase
): ViewModel(){
    private val route = savedStateHandle.toRoute<UserScreens.GroupDetailScreen>()
    private val groupId = GroupId(route.groupId) 
    
    private val _uiEvent = Channel<UiEvent<GroupDetailNavigation>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val uiState: StateFlow<UiState<GroupDetailScreenState>> = combine(
        flow = observeGroupById(groupId).distinctUntilChanged(),
        flow2 = observeGroupMembersPreview(groupId).distinctUntilChanged(),
        flow3 = observeUser().distinctUntilChanged(),
        flow4 = activeGroupSession.currentGroupId
    ) {group, members, currentUser, activeId ->
        if (group == null || currentUser == null) return@combine UiState.Empty

        val owner = members
            .find { it.role == AppRole.OWNER }
            ?.name ?: "Unknown"

        val currentUserRole = members
            .find { it.userId == currentUser.userId }
            ?.role ?: AppRole.MEMBER

        val groupDetail = GroupDetailState(
            name = group.name,
            description = group.description,
            totalProducts = group.totalProducts,
            totalMembers = group.totalMembers,
            address = group.address,
            owner = owner,
            created = group.createdAt.toFullDateString()
        )

        val memberItems = members.map { it.toMemberItem() }

        UiState.Success(
            GroupDetailScreenState(
                groupId = group.groupId.value,
                isActive = group.groupId.value == activeId,
                currentUserRole = currentUserRole,
                groupDetail = groupDetail,
                members = memberItems
            )
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    init {
        viewModelScope.launch {
            refreshGroupMembers(groupId)
        }
    }

    fun setGroupAsActive() {
        viewModelScope.launch {
            selectGroup(groupId)
            _uiEvent.send(UiEvent.ShowToast(R.string.success_group_set_as_active))
        }
    }

    fun checkDeleteValidity(onValid: () -> Unit) {
        viewModelScope.launch {
            val ownedGroupCount = ownedGroupCount()

            if (ownedGroupCount <= 1) {
                _uiEvent.send(UiEvent.ShowToast(R.string.error_group_last_cannot_delete))
            } else {
                onValid()
            }
        }
    }

    fun deleteGroup() {
        viewModelScope.launch {
            when(
                val result = deleteGroup(groupId)
            ) {
                is Result.Success -> {
                    _uiEvent.send(UiEvent.ShowToast(R.string.success_group_deleted))
                    _uiEvent.send(UiEvent.Navigate(GroupDetailNavigation.NavigateUp))
                }
                is Result.Error -> {
                    when (val netError = result.error) {
                        NetworkError.Local.NO_INTERNET -> {
                            _uiEvent.send(UiEvent.ShowToast(R.string.error_no_internet))
                        }
                        NetworkError.Local.UNKNOWN -> {
                            _uiEvent.send(UiEvent.ShowToast(R.string.error_unknown))
                        }
                        is NetworkError.Server -> {
                            _uiEvent.send(UiEvent.ShowToast(netError.message))
                        }
                        else -> {
                            _uiEvent.send(UiEvent.ShowToast(R.string.error_unknown))
                        }
                    }
                }
            }
        }
    }

}