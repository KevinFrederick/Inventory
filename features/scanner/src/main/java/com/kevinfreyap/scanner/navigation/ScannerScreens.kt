package com.kevinfreyap.scanner.navigation

import kotlinx.serialization.Serializable

sealed interface ScannerScreens {
    @Serializable
    data object ScannerScreen: ScannerScreens
}