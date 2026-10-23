package com.kevinfreyap.domain.manager

import kotlinx.coroutines.flow.Flow

interface INetworkMonitor {
    val isOnline: Flow<Boolean>
}