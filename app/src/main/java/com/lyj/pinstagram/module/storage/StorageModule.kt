package com.lyj.pinstagram.module.storage

import com.lyj.data.repository.network.StorageRepositoryImpl
import com.lyj.data.source.remote.storage.FirebaseStorageUploader
import com.lyj.domain.repository.network.StorageRepository
import com.lyj.domain.repository.network.StorageUploader
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
    fun providerStorageUploader(): StorageUploader = FirebaseStorageUploader()

    @Provides
    @Singleton
    fun providerStorageRepository(storageUploader: StorageUploader): StorageRepository =
        StorageRepositoryImpl(storageUploader)
}