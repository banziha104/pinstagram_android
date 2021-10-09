package com.lyj.domain.repository.android

import android.app.Activity
import io.reactivex.rxjava3.core.Single

typealias IsAllGranted = Boolean

interface PermissionRepository {
    fun checkAndRequestPermission(activity: Activity, permissions: Array<String>) : Single<Boolean>
}