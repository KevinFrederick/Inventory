package com.kevinfreyap.database.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.sessionDataStore by preferencesDataStore(name = "session_preferences")
private val Context.deviceDataStore by preferencesDataStore(name = "device_preferences")

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    @Singleton
    @SessionDataStore
    fun provideSessionDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.sessionDataStore
    }

    @Provides
    @Singleton
    @DeviceDataStore
    fun provideDeviceDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.deviceDataStore
    }
}