//package com.lyj.customui.dialog.permission
//
//import android.content.Context
//import androidx.appcompat.app.AppCompatActivity
//import com.lyj.core.module.size.SizeMeasurable
//import com.lyj.core.permission.PermissionManager
//
//class PermissionDialogViewModel(
//    override val context: AppCompatActivity,
//    val permissions : Array<String>
//) : SizeMeasurable{
//
//    fun requestPermission(){
//        PermissionManager(
//            context,
//            permissions
//        ).checkVersion()
//    }
//}