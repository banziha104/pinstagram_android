package com.lyj.pinstagram.view.splash

import android.Manifest
import android.app.Activity
import androidx.lifecycle.ViewModel
import com.lyj.core.permission.IsAllGranted
import com.lyj.core.permission.PermissionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val permissionManager: PermissionManager
) : ViewModel(){

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.INTERNET,
        Manifest.permission.CAMERA,
    )

    fun checkAndRequestPermission(activity: Activity) : Single<IsAllGranted> = permissionManager.checkAndRequestPermision(activity,permissions)
}