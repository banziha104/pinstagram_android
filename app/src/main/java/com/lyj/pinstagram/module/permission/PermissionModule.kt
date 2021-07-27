package com.lyj.pinstagram.module.permission

import com.lyj.core.permission.PermissionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class PermissionModule {
    @Provides
    @Singleton
    fun providePermissionManager() : PermissionManager = PermissionManager()
}