package com.lyj.domain.usecase.network.storage

import android.net.Uri
import com.lyj.core.extension.lang.SchedulerType
import com.lyj.core.extension.lang.applyScheduler
import com.lyj.domain.repository.network.StorageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestUploadStorageUseCase @Inject constructor(
    private val repository: StorageRepository
) {
    fun execute(uri : Uri) = repository.upload(uri).applyScheduler(subscribeOn = SchedulerType.IO)
}