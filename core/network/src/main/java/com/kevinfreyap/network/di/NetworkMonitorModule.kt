package com.kevinfreyap.network.di

import com.kevinfreyap.domain.manager.INetworkMonitor
import com.kevinfreyap.network.manager.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkMonitorModule {
    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(
        impl: NetworkMonitor
    ): INetworkMonitor
}