package com.kevinfreyap.user.di

import com.kevinfreyap.domain.repository.ICoreUserRepository
import com.kevinfreyap.domain.repository.IUserGroupRepository
import com.kevinfreyap.user.data.repository.GroupRepository
import com.kevinfreyap.user.data.repository.UserRepository
import com.kevinfreyap.user.domain.repository.IGroupRepository
import com.kevinfreyap.user.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {
    @Binds
    abstract fun bindUserRepository(
        impl: UserRepository
    ): IUserRepository

    @Binds
    abstract fun bindGroupRepository(
        impl: GroupRepository
    ): IGroupRepository

    @Binds
    @Singleton
    abstract fun bindUserGroupRepository(
        impl: GroupRepository
    ): IUserGroupRepository

    @Binds
    @Singleton
    abstract fun bindCoreUserRepository(
        impl: UserRepository
    ): ICoreUserRepository
}