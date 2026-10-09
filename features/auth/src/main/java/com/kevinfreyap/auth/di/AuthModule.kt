package com.kevinfreyap.auth.di

import com.kevinfreyap.auth.data.repository.AuthRepository
import com.kevinfreyap.domain.repository.IAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepository
    ): IAuthRepository
}