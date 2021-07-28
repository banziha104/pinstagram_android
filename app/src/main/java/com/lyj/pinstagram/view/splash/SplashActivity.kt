package com.lyj.pinstagram.view.splash

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.lyj.core.extension.android.plusAssign
import com.lyj.core.extension.lang.plusAssign
import com.lyj.core.extension.permissionTag
import com.lyj.core.permission.IsAllGranted
import com.lyj.core.rx.activity.AutoActivatedDisposable
import com.lyj.core.rx.activity.AutoClearedDisposable
import com.lyj.pinstagram.R
import com.lyj.pinstagram.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()
    private val disposables = AutoClearedDisposable(this)
    private val viewDisposables = AutoActivatedDisposable(lifecycleOwner = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycle += disposables
        lifecycle += viewDisposables

        viewDisposables += observePermission(viewModel.checkAndRequestPermission(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val denied = grantResults.filter { it == PackageManager.PERMISSION_DENIED }
        Log.d(permissionTag,grantResults.joinToString(","))
        Log.d(permissionTag,permissions.joinToString(","))
        if (denied.isNotEmpty()) {
            buildAlertDialog().show()
        }else{
            moveToMainActivity()
        }
    }


    private fun observePermission(
        single: Single<IsAllGranted>
    ): Disposable =
        single.subscribe({ isAllGranted ->
            Log.d(permissionTag,"isAllGranted :$isAllGranted")
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

    private fun buildAlertDialog() = AlertDialog.Builder(this)
        .setTitle(getString(R.string.splash_permission_dialog_title))
        .setMessage(getString(R.string.splash_permission_dialog_description))
        .setPositiveButton(getString(R.string.splash_permission_dialog_positive_title)) { dialog, _ ->
            viewDisposables += observePermission(viewModel.checkAndRequestPermission(this))
            dialog.dismiss()
        }
        .setNegativeButton(getString(R.string.splash_permission_dialog_negative_title)) { dialog, _ ->
            dialog.dismiss()
            finishAffinity()
        }
        .setCancelable(false)
}