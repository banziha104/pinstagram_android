package com.lyj.pinstagram.view.splash

import android.Manifest
import android.app.Activity
import androidx.lifecycle.ViewModel
import com.lyj.domain.repository.android.IsAllGranted
import com.lyj.domain.usecase.android.permission.PermissionCheckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val permissionCheckUseCase: PermissionCheckUseCase,
) : ViewModel() {

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.INTERNET,
        Manifest.permission.CAMERA,
    )

    fun checkAndRequestPermission(activity: Activity): Single<IsAllGranted> =
        permissionCheckUseCase.execute(activity, permissions)
}