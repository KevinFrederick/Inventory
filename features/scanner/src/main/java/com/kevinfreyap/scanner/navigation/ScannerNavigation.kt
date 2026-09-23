package com.kevinfreyap.scanner.navigation

sealed interface ScannerNavigation {
    data object NavigateUp: ScannerNavigation
}