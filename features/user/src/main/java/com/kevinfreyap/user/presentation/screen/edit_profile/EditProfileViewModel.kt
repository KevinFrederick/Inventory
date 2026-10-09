package com.kevinfreyap.user.presentation.screen.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.user.R
import com.kevinfreyap.user.domain.model.UserUpdateParams
import com.kevinfreyap.user.domain.usecase.user.ObserveUserUseCase
import com.kevinfreyap.user.domain.usecase.user.UpdateUserProfileUseCase
import com.kevinfreyap.user.presentation.action.EditProfileAction
import com.kevinfreyap.user.presentation.navigation.EditProfileNavigation
import com.kevinfreyap.user.presentation.state.EditProfileScreenState
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
class EditProfileViewModel @Inject constructor(
    private val observeUser: ObserveUserUseCase,
    private val updateUserProfile: UpdateUserProfileUseCase
): ViewModel(){
    private val _uiEvent = Channel<UiEvent<EditProfileNavigation>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _formState = MutableStateFlow(EditProfileScreenState())
    val formState = _formState.asStateFlow()

    init {
        loadInitialData()
    }

    fun onAction (action: EditProfileAction) {
        when(action) {
            is EditProfileAction.OnImageUrlChanged -> handleProfileUrl(action)
            is EditProfileAction.UserDetailAction -> handleProfileUpdate(action)
            is EditProfileAction.UpdateProfile -> updateProfile()
            is EditProfileAction.ProfilePictureAction -> {}
        }
    }

    private fun loadInitialData() {
        _formState.update { it.copy(uiState = UiState.Loading) }

        viewModelScope.launch {
            val user = observeUser().filterNotNull().first()

            _formState.update { currentState ->
                val initialUserDetail = currentState.userDetail.copy(
                    name = user.name,
                    email = user.email,
                    profileUrl = user.profilePictureUrl,
                    phoneNumber = user.phoneNumber,
                    jobTitle = user.jobTitle
                )

                currentState.copy(
                    originalUserDetail = initialUserDetail,
                    userDetail = initialUserDetail,
                    uiState = UiState.Idle
                )
            }
        }
    }

    private fun handleProfileUrl(action: EditProfileAction.OnImageUrlChanged) {
        _formState.update { currentState ->
            currentState.copy(
                userDetail = currentState.userDetail.copy(profileUrl = action.url),
                formErrors = currentState.formErrors?.copy(urlError = null)
            )
        }
    }

    private fun handleProfileUpdate(action: EditProfileAction.UserDetailAction) {
        when (action) {
            is EditProfileAction.UserDetailAction.OnNameChanged -> {
                _formState.update { currentState ->
                    currentState.copy(
                        userDetail = currentState.userDetail.copy(name = action.name),
                        formErrors = currentState.formErrors?.copy(nameError = null)
                    )
                }
            }
            is EditProfileAction.UserDetailAction.OnPhoneNumberChanged -> {
                _formState.update { currentState ->
                    currentState.copy(
                        userDetail = currentState.userDetail.copy(phoneNumber = action.phoneNumber),
                        formErrors = currentState.formErrors?.copy(phoneNumberError = null)
                    )
                }
            }
            is EditProfileAction.UserDetailAction.OnJobTitleChanged -> {
                _formState.update { currentState ->
                    currentState.copy(
                        userDetail = currentState.userDetail.copy(jobTitle = action.jobTitle),
                        formErrors = currentState.formErrors?.copy(jobTitleError = null)
                    )
                }
            }
        }
    }

    private fun updateProfile() {
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

                val result = updateUserProfile(
                    req = UserUpdateParams(
                        name = currentState.userDetail.name,
                        profilePictureUrl = currentState.userDetail.profileUrl,
                        phoneNumber = currentState.userDetail.phoneNumber,
                        jobTitle = currentState.userDetail.jobTitle,
                        locale = null,
                        timeZone = null
                    )
                )

                when(result) {
                    is Result.Success -> {
                        _formState.update { currentState->
                            currentState.copy(
                                uiState = UiState.Success(Unit)
                            )
                        }

                        _uiEvent.send(UiEvent.ShowToast(R.string.success_profile_update))
                        _uiEvent.send(UiEvent.Navigate(EditProfileNavigation.NavigateUp))

                        _formState.update { EditProfileScreenState() }
                    }
                    is Result.Error -> {
                        val error = result.error

                        when (val netError = error.networkError) {
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
                                _uiEvent.send(UiEvent.ShowToast(netError.message))
                            }
                            null -> {}
                        }

                        _formState.update {
                            it.copy(
                                formErrors = error,
                                uiState = UiState.Idle,
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