package com.kevinfreyap.user.presentation.screen.group_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.user.R
import com.kevinfreyap.user.domain.model.GroupParams
import com.kevinfreyap.user.domain.usecase.group.CreateGroupUseCase
import com.kevinfreyap.user.domain.usecase.group.ObserveGroupByIdUseCase
import com.kevinfreyap.user.domain.usecase.group.UpdateGroupUseCase
import com.kevinfreyap.user.presentation.action.GroupFormAction
import com.kevinfreyap.user.presentation.navigation.GroupFormNavigation
import com.kevinfreyap.user.presentation.navigation.UserScreens
import com.kevinfreyap.user.presentation.state.GroupFormDetailState
import com.kevinfreyap.user.presentation.state.GroupFormScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeGroupById: ObserveGroupByIdUseCase,
    private val createGroupUseCase: CreateGroupUseCase,
    private val updateGroupUseCase: UpdateGroupUseCase
) : ViewModel() {

    private val route = savedStateHandle.toRoute<UserScreens.GroupFormScreen>()

    private val _uiEvent = Channel<UiEvent<GroupFormNavigation>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _formState = MutableStateFlow(GroupFormScreenState())
    val formState = _formState.asStateFlow()

    init {
        loadInitialData()
    }

    fun onAction(action: GroupFormAction) {
        when (action) {
            is GroupFormAction.OnNameChanged -> handleNameChange(action.name)
            is GroupFormAction.OnDescriptionChanged -> handleDescriptionChange(action.description)
            is GroupFormAction.OnAddressChanged -> handleAddressChange(action.address)
            is GroupFormAction.Submit -> submitGroup()
        }
    }

    private fun loadInitialData() {
        val groupId = route.groupId
        if (groupId.isNullOrBlank()) {
            _formState.update {
                it.copy(
                    groupId = null,
                    isEditMode = false,
                    uiState = UiState.Idle
                )
            }
            return
        }

        _formState.update {
            it.copy(
                groupId = groupId,
                isEditMode = true,
                uiState = UiState.Loading
            )
        }

        viewModelScope.launch {
            val group = observeGroupById(GroupId(groupId)).filterNotNull().first()

            val initialGroupDetail = GroupFormDetailState(
                name = group.name,
                description = group.description ?: "",
                address = group.address ?: ""
            )

            _formState.update { currentState ->
                currentState.copy(
                    originalGroupDetail = initialGroupDetail,
                    groupDetail = initialGroupDetail,
                    uiState = UiState.Idle
                )
            }
        }
    }

    private fun handleNameChange(name: String) {
        _formState.update { currentState ->
            currentState.copy(
                groupDetail = currentState.groupDetail.copy(name = name),
                formErrors = currentState.formErrors?.copy(groupNameError = null)
            )
        }
    }

    private fun handleDescriptionChange(description: String) {
        _formState.update { currentState ->
            currentState.copy(
                groupDetail = currentState.groupDetail.copy(description = description),
                formErrors = currentState.formErrors?.copy(groupDescriptionError = null)
            )
        }
    }

    private fun handleAddressChange(address: String) {
        _formState.update { currentState ->
            currentState.copy(
                groupDetail = currentState.groupDetail.copy(address = address),
                formErrors = currentState.formErrors?.copy(groupAddressError = null)
            )
        }
    }

    private fun submitGroup() {
        if (_formState.value.uiState == UiState.Loading) return

        _formState.update { currentState ->
            currentState.copy(
                formErrors = null,
                uiState = UiState.Loading
            )
        }

        viewModelScope.launch {
            try {
                val currentState = _formState.value
                val groupParams = GroupParams(
                    name = currentState.groupDetail.name,
                    description = currentState.groupDetail.description.ifBlank { null },
                    address = currentState.groupDetail.address.ifBlank { null }
                )

                val result = if (currentState.isEditMode && !currentState.groupId.isNullOrBlank()) {
                    updateGroupUseCase(
                        groupId = GroupId(currentState.groupId),
                        newGroup = groupParams
                    )
                } else {
                    createGroupUseCase(
                        newGroup = groupParams
                    )
                }

                when (result) {
                    is Result.Success -> {
                        _formState.update {
                            it.copy(uiState = UiState.Success(Unit))
                        }

                        val successMessageRes = if (currentState.isEditMode) {
                            R.string.success_group_updated
                        } else {
                            R.string.success_group_created
                        }

                        _uiEvent.send(UiEvent.ShowToast(successMessageRes))
                        _uiEvent.send(UiEvent.Navigate(GroupFormNavigation.NavigateUp))

                        _formState.update { GroupFormScreenState() }
                    }
                    is Result.Error -> {
                        val error = result.error

                        when (val netError = error.networkError) {
                            NetworkError.Local.NO_INTERNET -> {
                                _uiEvent.send(UiEvent.ShowToast(R.string.error_no_internet))
                            }
                            NetworkError.Local.UNKNOWN -> {
                                _uiEvent.send(UiEvent.ShowToast(R.string.error_unknown))
                            }
                            is NetworkError.Server -> {
                                _uiEvent.send(UiEvent.ShowToast(netError.message))
                            }
                            else -> {}
                        }

                        _formState.update {
                            it.copy(
                                formErrors = error,
                                uiState = UiState.Idle
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _formState.update {
                    it.copy(
                        uiState = UiState.Error(
                            message = e.localizedMessage ?: "An error occurred"
                        )
                    )
                }
            }
        }
    }
}