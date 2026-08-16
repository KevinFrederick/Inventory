package com.kevinfreyap.inventory

import kotlinx.serialization.Serializable

sealed interface AppGraph {
    @Serializable
    data object Auth: AppGraph

    @Serializable
    data object Main: AppGraph
}
