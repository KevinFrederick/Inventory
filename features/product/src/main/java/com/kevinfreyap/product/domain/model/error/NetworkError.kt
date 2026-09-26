package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.RootError

sealed interface NetworkError: RootError {
    enum class Local: NetworkError{
        NO_INTERNET,
        UNKNOWN
    }

    data class Server(val message: String): NetworkError
}