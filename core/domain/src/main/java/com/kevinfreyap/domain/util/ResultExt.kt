package com.kevinfreyap.domain.util

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.RootError

fun <D> Result<D, *>.getOrNull(): D? = when (this) {
    is Result.Success -> data
    is Result.Error -> null
}

fun <E: RootError> Result<*, E>.errorOrNull(): E? = when (this) {
    is Result.Success -> null
    is Result.Error -> error
}