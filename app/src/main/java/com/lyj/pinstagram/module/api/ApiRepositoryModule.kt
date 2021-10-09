package com.lyj.pinstagram.module.api

import com.lyj.data.repository.local.TokenRepositoryImpl
import com.lyj.data.repository.network.AuthRepositoryImpl
import com.lyj.data.repository.network.GeometryRepositoryImpl
import com.lyj.data.source.local.LocalDatabase
import com.lyj.data.source.remote.http.auth.AuthenticationService
import com.lyj.data.source.remote.http.geo.GeometryService
import com.lyj.domain.repository.local.TokenRepository
import com.lyj.domain.repository.network.AuthRepository
import com.lyj.domain.repository.network.GeometryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiRepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authenticationService: AuthenticationService
    ): AuthRepository = AuthRepositoryImpl(authenticationService)

    @Provides
    @Singleton
    fun provideGeometryRepository(
        geometryService: GeometryService
    ): GeometryRepository = GeometryRepositoryImpl(geometryService)
}