package com.kevinfreyap.domain

import com.kevinfreyap.domain.error.RootError

sealed interface Result <out D, out E: RootError> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E: RootError>(val error: E): Result<Nothing, E>
}