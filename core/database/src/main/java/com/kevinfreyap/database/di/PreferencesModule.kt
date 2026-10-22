package com.kevinfreyap.database.di

import com.kevinfreyap.database.preferences.DevicePreferences
import com.kevinfreyap.database.preferences.SyncPreferences
import com.kevinfreyap.domain.preferences.IDevicePreferences
import com.kevinfreyap.domain.preferences.ISyncPreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {
    @Binds
    abstract fun provideSyncPreferences(
        impl: SyncPreferences
    ): ISyncPreferences

    @Binds
    abstract fun provideDevicePreferences(
        impl: DevicePreferences
    ): IDevicePreferences
}