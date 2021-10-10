package com.lyj.domain.usecase.network.storage

import android.net.Uri
import com.lyj.domain.repository.network.StorageRepository
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestUploadStorageUseCase @Inject constructor(
    private val repository: StorageRepository
) {
    fun execute(uri: Uri) = repository.upload(uri).subscribeOn(Schedulers.io())
}