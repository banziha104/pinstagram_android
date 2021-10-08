package com.lyj.pinstagram.module.repository

import com.lyj.data.mapper.TokenMapper
import com.lyj.data.repository.local.TokenRepositoryImpl
import com.lyj.data.source.local.LocalDatabase
import com.lyj.domain.repository.local.TokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class TokenModule {

    @Provides
    @Singleton
    fun provideTokenRepository(
        database: LocalDatabase,
        mapper: TokenMapper
    ): TokenRepository = TokenRepositoryImpl(mapper,database.tokenDao())
}