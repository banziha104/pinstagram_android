package com.lyj.pinstagram.module.location

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import com.lyj.core.permission.PermissionManager
import com.lyj.pinstagram.location.LocationEventManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationEventManagerModule {

    @Provides
    @Singleton
    fun providePermissionManager(
        @ApplicationContext context : Context,
        permissionManager: PermissionManager
    ) : LocationEventManager = LocationEventManager(context,permissionManager)

}