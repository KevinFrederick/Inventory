package com.kevinfreyap.user.di

import com.kevinfreyap.user.data.repository.GroupRepository
import com.kevinfreyap.user.data.repository.UserRepository
import com.kevinfreyap.user.domain.repository.IGroupRepository
import com.kevinfreyap.user.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
}