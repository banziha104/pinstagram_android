package com.lyj.pinstagram.view.activities.splash

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.lyj.core.extension.permissionTag
import com.lyj.core.extension.simpleTag
import com.lyj.core.permission.PermissionManager
import com.lyj.pinstagram.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(
            permissionTag,
            "onRequestPermissionsResult $requestCode /  ${permissions.joinToString(",")} / ${
                grantResults.joinToString(",")
            }"
        )
    }
}
}