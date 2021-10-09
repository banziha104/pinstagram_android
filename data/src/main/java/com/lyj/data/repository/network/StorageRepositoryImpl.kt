package com.lyj.data.repository.network

import com.lyj.domain.repository.network.StorageRepository
import com.lyj.domain.repository.network.StorageUploader

class StorageRepositoryImpl(
    private val storageUploader: StorageUploader
) : StorageRepository, StorageUploader by storageUploader