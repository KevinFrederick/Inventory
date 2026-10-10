package com.kevinfreyap.database.di

import com.kevinfreyap.database.manager.AuthTokenManager
import com.kevinfreyap.database.manager.ImageManager
import com.kevinfreyap.domain.manager.IAuthTokenManager
import com.kevinfreyap.domain.manager.IImageManager
import dagger.Binds
import dagger.Module
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
}