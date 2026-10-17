package com.kevinfreyap.database.di

import com.kevinfreyap.database.datastore.SyncPreferences
import com.kevinfreyap.database.manager.AuthTokenManager
import com.kevinfreyap.database.manager.ImageManager
import com.kevinfreyap.domain.manager.IAuthTokenManager
import com.kevinfreyap.domain.manager.IImageManager
import com.kevinfreyap.domain.preferences.ISyncPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {
    @Binds
    @Singleton
    abstract fun bindAuthTokenManager(
        authTokenManager: AuthTokenManager
    ): IAuthTokenManager

    @Binds
    abstract fun bindImageManager(
        impl: ImageManager
    ): IImageManager

    @Binds
    abstract fun provideSyncPreferences(
        impl: SyncPreferences
    ): ISyncPreferences
}