package com.lyj.data.repository.android

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import com.lyj.domain.repository.android.IsAllGranted
import com.lyj.domain.repository.android.PermissionRepository
import io.reactivex.rxjava3.core.Single


class PermissionRepositoryImpl: PermissionRepository{
    companion object{
        const val REQUEST_CODE = 10001
    }

    override fun checkAndRequestPermission(activity: Activity, permissions: Array<String>) : Single<IsAllGranted> = Single.create{ emitter ->
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            emitter.onSuccess(true)
        }else{
            val permissionMap = permissions
                .map { it to activity.checkSelfPermission(it) }
                .groupBy { it.second }

            if (permissionMap[PackageManager.PERMISSION_DENIED] != null) {
                activity.requestPermissions(permissionMap[PackageManager.PERMISSION_DENIED]!!.map { it.first }
                    .toTypedArray(), REQUEST_CODE)
                emitter.onSuccess(false)
            } else {
                emitter.onSuccess(true)
            }
        }
    }
}