package com.kevinfreyap.user.presentation.screen.group_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.util.toFullDateString
import com.kevinfreyap.user.domain.usecase.group.ObserveGroupByIdUseCase
import com.kevinfreyap.user.domain.usecase.group.ObserveGroupMembersUseCase
import com.kevinfreyap.user.domain.usecase.user.ObserveUserUseCase
import com.kevinfreyap.user.presentation.mapper.toMemberItem
import com.kevinfreyap.user.presentation.navigation.GroupDetailNavigation
import com.kevinfreyap.user.presentation.navigation.ProfileNavigation
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
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    observeGroupById: ObserveGroupByIdUseCase,
    observeGroupMembers: ObserveGroupMembersUseCase,
    observeUser: ObserveUserUseCase,
    activeGroupSession: IActiveGroupSession,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val route = savedStateHandle.toRoute<UserScreens.GroupDetailScreen>()
    private val groupId = GroupId(route.groupId) 
    
    private val _uiEvent = Channel<UiEvent<GroupDetailNavigation>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val uiState: StateFlow<UiState<GroupDetailScreenState>> = combine(
        flow = observeGroupById(groupId).distinctUntilChanged(),
        flow2 = observeGroupMembers(groupId).distinctUntilChanged(),
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
            name = group.group.name,
            description = group.group.description,
            totalProducts = group.totalProducts,
            totalMembers = group.totalMembers,
            address = group.group.address,
            owner = owner,
            created = group.group.createdAt.toFullDateString()
        )

        val memberItems = members.map { it.toMemberItem() }

        UiState.Success(
            GroupDetailScreenState(
                isActive = group.group.groupId.value == activeId,
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
}