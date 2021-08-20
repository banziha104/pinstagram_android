package com.lyj.pinstagram.module.jwt

import com.lyj.api.jwt.JwtManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class JwtManagerModule {

    @Provides
    @ViewModelScoped
    fun providerJwtModule(): JwtManager = JwtManager()
}