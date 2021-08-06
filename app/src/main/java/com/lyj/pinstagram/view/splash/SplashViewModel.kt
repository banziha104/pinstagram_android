package com.lyj.pinstagram.view.splash

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.ViewModel
import com.lyj.core.permission.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val permissionManager: PermissionManager
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
        permissionManager.checkAndRequestPermision(activity, permissions)

    fun buildPermissionAlertDialog(
        context: Context,
        positiveEvent: DialogCallBack,
        negetiveEvent: DialogCallBack
    ) = permissionManager.buildAlertDialog(context, positiveEvent, negetiveEvent)
}