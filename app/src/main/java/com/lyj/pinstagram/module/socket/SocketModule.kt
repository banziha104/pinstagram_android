package com.lyj.pinstagram.module.socket

import com.lyj.data.repository.network.TalkRepositoryImpl
import com.lyj.data.source.remote.http.talk.TalkService
import com.lyj.data.source.remote.socket.SocketManager
import com.lyj.domain.repository.network.SocketFactory
import com.lyj.domain.repository.network.TalkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class SocketModule {
    @Provides
    @Singleton
    fun provideSocketFactory() : SocketFactory =  SocketManager.Factory

    @Provides
    @Singleton
    fun provideTalkRepository(
        socketFactory: SocketFactory,
        talkService: TalkService
    ): TalkRepository = TalkRepositoryImpl(talkService,socketFactory)
}