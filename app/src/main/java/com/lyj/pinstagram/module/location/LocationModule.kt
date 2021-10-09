package com.lyj.pinstagram.module.location

import android.content.Context
import com.lyj.data.repository.android.LocationRepositoryImpl
import com.lyj.domain.repository.android.LocationRepository
import com.lyj.domain.repository.android.PermissionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {

    @Provides
    @Singleton
    fun provideLocationEventManager(
        @ApplicationContext context : Context,
        permissionManager: PermissionRepository
    ) : LocationRepository = LocationRepositoryImpl(context,permissionManager)
}