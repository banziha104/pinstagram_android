package com.lyj.pinstagram.module.permission

import android.content.Context
import com.lyj.data.source.android.location.GeoCodeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GeoCoderModule {

    @Provides
    @Singleton
    fun provideGeoCodeManager(
        @ApplicationContext context : Context
    ) : GeoCodeManager = GeoCodeManager(context)
}