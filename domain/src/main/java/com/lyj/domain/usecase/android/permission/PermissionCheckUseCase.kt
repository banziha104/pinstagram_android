package com.lyj.domain.usecase.android.permission

import android.app.Activity
import com.lyj.core.extension.lang.SchedulerType
import com.lyj.core.extension.lang.applyScheduler
import com.lyj.domain.repository.android.IsAllGranted
import com.lyj.domain.repository.android.PermissionRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionCheckUseCase @Inject constructor(
    private val permissionRepository: PermissionRepository
) {
    fun execute(
        activity: Activity,
        permissions: Array<String>
    ): Single<IsAllGranted> =
        permissionRepository
            .checkAndRequestPermission(activity, permissions)
            .applyScheduler(observeOn = SchedulerType.MAIN)
}