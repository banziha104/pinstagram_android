package com.lyj.pinstagram.module.storage

import com.lyj.data.source.remote.storage.FirebaseStorageUploader
import com.lyj.data.source.remote.storage.StorageUploader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {
    @Provides
    @Singleton
    fun providerStorageUploader() : StorageUploader = FirebaseStorageUploader()
}