package com.lyj.pinstagram.view.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lyj.core.extension.lang.permissionTag
import com.lyj.core.rx.*
import com.lyj.domain.repository.android.IsAllGranted
import com.lyj.pinstagram.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable


typealias DialogCallBack = (DialogInterface, Int) -> Unit

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity(), DisposableLifecycleController {

    override val disposableLifecycleObserver: RxLifecycleObserver =
        RxLifecycleObserver(this)
    val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observePermission(viewModel.checkAndRequestPermission(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val denied = grantResults.filter { it == PackageManager.PERMISSION_DENIED }
        if (denied.isNotEmpty()) {
            buildAlertDialog(
                this,
                { dialog, _ ->
                    viewModel.checkAndRequestPermission(
                        this
                    ).subscribe({}, { it.printStackTrace() })
                    dialog.dismiss()
                },
                { dialog, _ ->
                    dialog.dismiss()
                    finishAffinity()
                }
            ).show()
        } else {
            moveToMainActivity()
        }
    }


    private fun buildAlertDialog(
        context: Context,
        positiveEvent: DialogCallBack,
        negativeEvent: DialogCallBack
    ): AlertDialog.Builder = AlertDialog.Builder(context)
        .setTitle(context.getString(com.lyj.core.R.string.permission_dialog_title))
        .setMessage(context.getString(com.lyj.core.R.string.permission_dialog_description))
        .setPositiveButton(
            context.getString(com.lyj.core.R.string.permission_dialog_positive_title),
            positiveEvent
        )
        .setNegativeButton(
            context.getString(com.lyj.core.R.string.permission_dialog_negative_title),
            negativeEvent
        )
        .setCancelable(false)


    private fun observePermission(
        single: Single<IsAllGranted>
    ): Disposable =
        single
            .disposeByOnDestory(this)
            .subscribe({ isAllGranted ->
                Log.d(permissionTag, "isAllGranted :$isAllGranted")
                if (isAllGranted) {
                    moveToMainActivity()
                }
            }, {
                it.printStackTrace()
            })


    private fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}