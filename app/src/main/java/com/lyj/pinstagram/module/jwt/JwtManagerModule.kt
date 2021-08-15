package com.lyj.pinstagram.module.jwt

import android.content.Context
import androidx.room.Room
import com.lyj.api.database.LocalDatabase
import com.lyj.api.jwt.JwtManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class JwtManagerModule {

    @Provides
    @Singleton
    fun providerJwtModule(): JwtManager = JwtManager()
}