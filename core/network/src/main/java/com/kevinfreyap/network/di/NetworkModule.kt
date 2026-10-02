package com.kevinfreyap.network.di

import com.kevinfreyap.domain.manager.IAuthTokenManager
import com.kevinfreyap.network.client.provideKtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(
        tokenManager: IAuthTokenManager
    ): HttpClient {
        return provideKtorClient(tokenManager)
    }
}