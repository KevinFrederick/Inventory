package com.kevinfreyap.auth.di

import com.kevinfreyap.auth.data.repository.AuthRepository
import com.kevinfreyap.auth.domain.repository.IAuthRepository
import com.kevinfreyap.database.manager.AuthTokenManager
import com.kevinfreyap.domain.manager.IAuthTokenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepository
    ): IAuthRepository
}