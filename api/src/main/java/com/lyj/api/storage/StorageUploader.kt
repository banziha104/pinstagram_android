package com.lyj.api.storage

import android.net.Uri
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface StorageUploader {
    fun upload(uri : Uri) : Single<String>
}