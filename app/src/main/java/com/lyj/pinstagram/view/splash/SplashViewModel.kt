package com.lyj.pinstagram.view.splash

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.lyj.core.permission.DialogCallBack
import com.lyj.core.permission.IsAllGranted
import com.lyj.core.permission.PermissionManager
import com.lyj.domain.usecase.TokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val permissionManager: PermissionManager,
    val tokenUseCase: TokenUseCase
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
        permissionManager.checkAndRequestPermission(activity, permissions)

    fun buildPermissionAlertDialog(
        context: Context,
        positiveEvent: DialogCallBack,
        negetiveEvent: DialogCallBack
    ) = permissionManager.buildAlertDialog(context, positiveEvent, negetiveEvent)
}