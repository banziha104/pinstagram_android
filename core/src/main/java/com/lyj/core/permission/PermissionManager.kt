package com.lyj.core.permission

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import com.lyj.core.R
import io.reactivex.rxjava3.core.Single

typealias IsAllGranted = Boolean
typealias DialogCallBack = (DialogInterface, Int) -> Unit
class PermissionManager(private val context : Context) : PermissionChecker, PermissionDialogBuilder {
    companion object{
        const val REQUEST_CODE = 10001
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun checkAndRequestPermission(activity: Activity, permissions: Array<String>) : Single<IsAllGranted> = Single.create{ emitter ->
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            emitter.onSuccess(true)
        }else{
            val permissionMap = permissions
                .map { it to activity.checkSelfPermission(it) }
                .groupBy { it.second }

            if (permissionMap[PackageManager.PERMISSION_DENIED] != null) {
                activity.requestPermissions(permissionMap[PackageManager.PERMISSION_DENIED]!!.map { it.first }
                    .toTypedArray(), REQUEST_CODE)
                emitter.onSuccess(false)
            } else {
                emitter.onSuccess(true)
            }
        }
    }

    override fun buildAlertDialog(context: Context, positiveEvent : DialogCallBack, negativeEvent : DialogCallBack): AlertDialog.Builder = AlertDialog.Builder(context)
        .setTitle(context.getString(R.string.permission_dialog_title))
        .setMessage(context.getString(R.string.permission_dialog_description))
        .setPositiveButton(context.getString(R.string.permission_dialog_positive_title),positiveEvent)
        .setNegativeButton(context.getString(R.string.permission_dialog_negative_title),negativeEvent)
        .setCancelable(false)
}