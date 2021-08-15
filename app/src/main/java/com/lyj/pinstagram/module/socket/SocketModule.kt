package com.lyj.pinstagram.module.socket

import com.lyj.api.socket.SocketContract
import com.lyj.api.socket.SocketManager
import com.lyj.api.storage.FirebaseStorageUploader
import com.lyj.api.storage.StorageUploader
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
    fun provideSocketFactory() : SocketManager.Factory =  SocketManager.Factory()
}