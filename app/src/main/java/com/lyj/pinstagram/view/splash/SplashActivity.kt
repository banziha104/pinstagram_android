package com.lyj.pinstagram.view.splash

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.lyj.core.base.BaseActivity
import com.lyj.core.extension.android.plusAssign
import com.lyj.core.extension.lang.plusAssign
import com.lyj.core.extension.permissionTag
import com.lyj.core.permission.DialogCallBack
import com.lyj.core.permission.IsAllGranted
import com.lyj.core.rx.activity.AutoActivatedDisposable
import com.lyj.core.rx.activity.AutoClearedDisposable
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivitySplashBinding
import com.lyj.pinstagram.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable


@AndroidEntryPoint
class SplashActivity : BaseActivity<SplashViewModel,ActivitySplashBinding>(R.layout.activity_splash) {

    override val viewModel : SplashViewModel by viewModels()

    val positive : DialogCallBack
    inline get() = { dialog, which ->

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            viewModel.buildPermissionAlertDialog(
                this,
                positiveEvent = { dialog, _ ->
                    viewDisposables += observePermission(viewModel.checkAndRequestPermission(this))
                    dialog.dismiss()
                },
                negetiveEvent = { dialog, _ ->
                    dialog.dismiss()
                    finishAffinity()
                }
            ).show()
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
}