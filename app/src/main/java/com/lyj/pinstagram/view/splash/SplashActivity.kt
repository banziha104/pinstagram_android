package com.lyj.pinstagram.view.splash

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.lyj.core.base.BaseActivity
import com.lyj.core.extension.android.fromStartToStopScope
import com.lyj.core.extension.lang.SchedulerType
import com.lyj.core.extension.lang.applyScheduler
import com.lyj.core.extension.lang.permissionTag
import com.lyj.core.extension.lang.testTag
import com.lyj.core.permission.IsAllGranted
import com.lyj.core.rx.DisposableFunction
import com.lyj.core.rx.plusAssign
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivitySplashBinding
import com.lyj.pinstagram.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Single


@AndroidEntryPoint
class SplashActivity :
    BaseActivity<SplashViewModel, ActivitySplashBinding>(R.layout.activity_splash,
        { ActivitySplashBinding.inflate(it) }) {

    override val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(testTag, "정상작동? 11")
        viewModel.tokenUseCase.tokenRepository.findToken().applyScheduler(subscribeOn = SchedulerType.IO,observeOn = SchedulerType.IO).subscribe({
            Log.d(testTag, "정상작동? $it")
        }, {
            it.printStackTrace()
        })
        fromStartToStopScope += observePermission(viewModel.checkAndRequestPermission(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val denied = grantResults.filter { it == PackageManager.PERMISSION_DENIED }
        Log.d(permissionTag, grantResults.joinToString(","))
        Log.d(permissionTag, permissions.joinToString(","))
        if (denied.isNotEmpty()) {
            viewModel.buildPermissionAlertDialog(
                this,
                positiveEvent = { dialog, _ ->
                    fromStartToStopScope += observePermission(
                        viewModel.checkAndRequestPermission(
                            this
                        )
                    )
                    dialog.dismiss()
                },
                negetiveEvent = { dialog, _ ->
                    dialog.dismiss()
                    finishAffinity()
                }
            ).show()
        } else {
            moveToMainActivity()
        }
    }


    private fun observePermission(
        single: Single<IsAllGranted>
    ): DisposableFunction = {
        single.subscribe({ isAllGranted ->
            Log.d(permissionTag, "isAllGranted :$isAllGranted")
            if (isAllGranted) {
                moveToMainActivity()
            }
        }, {
            it.printStackTrace()
        })
    }


    private fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}