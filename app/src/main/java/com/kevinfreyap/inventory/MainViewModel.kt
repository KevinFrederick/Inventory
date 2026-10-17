package com.kevinfreyap.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinfreyap.domain.manager.IAuthTokenManager
import com.kevinfreyap.product.domain.usecase.ObserveRealTimeUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    tokenManager: IAuthTokenManager,
    private val observerRealTimeUpdate: ObserveRealTimeUpdateUseCase
): ViewModel() {

    val isLoading: StateFlow<Boolean> = tokenManager.tokenState
        .map { false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val isLoggedIn: StateFlow<Boolean> = tokenManager.tokenState
        .map { token -> !token.isNullOrBlank() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = tokenManager.fetchAccessToken() != null
        )

    suspend fun startListeningForSync() {
        observerRealTimeUpdate()
    }
}