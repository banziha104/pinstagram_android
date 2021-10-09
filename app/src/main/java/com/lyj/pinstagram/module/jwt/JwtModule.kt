package com.lyj.pinstagram.module.jwt

import com.lyj.data.common.jwt.JwtAuthData
import com.lyj.data.common.jwt.JwtManager
import com.lyj.data.repository.network.JwtRepositoryImpl
import com.lyj.domain.repository.network.JwtRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class JwtModule {

    @Provides
    @Singleton
    fun providerJwtRepository(): JwtRepository = JwtRepositoryImpl()
}