package com.kevinfreyap.ui.event

import androidx.annotation.StringRes

sealed interface UiEvent<out T> {
    data class Navigate <T> (val destination: T): UiEvent<T>
    data class ShowToast(@param:StringRes val messageRes: Int): UiEvent<Nothing>
}