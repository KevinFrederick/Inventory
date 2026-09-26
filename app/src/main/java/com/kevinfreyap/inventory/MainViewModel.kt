package com.kevinfreyap.inventory

import androidx.lifecycle.ViewModel
import com.kevinfreyap.product.domain.usecase.ObserveRealTimeUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val observerRealTimeUpdate: ObserveRealTimeUpdateUseCase
): ViewModel() {
    suspend fun startListeningForSync() {
        observerRealTimeUpdate()
    }
}