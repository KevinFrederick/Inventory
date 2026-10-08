package com.kevinfreyap.database.di

import com.kevinfreyap.database.session.ActiveGroupSession
import com.kevinfreyap.domain.session.IActiveGroupSession
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionModule {
    @Binds
    @Singleton
    abstract fun bindActiveGroupSession(
        impl: ActiveGroupSession
    ): IActiveGroupSession
}