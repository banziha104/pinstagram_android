package com.lyj.core.permission

import android.app.Activity
import io.reactivex.rxjava3.core.Single

interface PermissionChecker {
    fun checkAndRequestPermision(activity: Activity, permissions: Array<String>) : Single<IsAllGranted>
}