package com.lyj.pinstagram.module.permission

import com.lyj.core.permission.PermissionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class PermissionModule {

    @Provides
    @ViewModelScoped
    fun providePermissionManager() : PermissionManager = PermissionManager()
}