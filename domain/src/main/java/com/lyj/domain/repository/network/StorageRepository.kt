package com.lyj.domain.repository.network

import android.net.Uri
import io.reactivex.rxjava3.core.Single

interface StorageRepository : StorageUploader

interface StorageUploader {
    fun upload(uri : Uri) : Single<String>
}