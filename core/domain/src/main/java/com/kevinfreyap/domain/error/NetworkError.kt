package com.kevinfreyap.domain.error

sealed interface NetworkError: RootError {
    enum class Local: NetworkError{
        MISSING_FILE,
        NO_INTERNET,
        UNKNOWN
    }

    data class Server(val message: String): NetworkError
}