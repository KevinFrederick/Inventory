package com.kevinfreyap.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinfreyap.auth.domain.usecase.CheckUserLoggedInUseCase
import com.kevinfreyap.product.domain.usecase.ObserveRealTimeUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val observerRealTimeUpdate: ObserveRealTimeUpdateUseCase,
    private val checkUserLoggedIn: CheckUserLoggedInUseCase
): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        verifySession()
    }

    suspend fun startListeningForSync() {
        observerRealTimeUpdate()
    }

    private fun verifySession() {
        viewModelScope.launch {
            _isLoggedIn.value = checkUserLoggedIn()
            _isLoading.value = false
        }
    }
}