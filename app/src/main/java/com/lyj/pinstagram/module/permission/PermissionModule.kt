package com.lyj.pinstagram.module.permission

import com.lyj.data.repository.android.PermissionRepositoryImpl
import com.lyj.domain.repository.android.PermissionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PermissionModule {

    @Provides
    @Singleton
    fun providePermissionRepository() : PermissionRepository = PermissionRepositoryImpl()
}