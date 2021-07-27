//package com.lyj.customui.dialog.permission
//
//import android.app.Dialog
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import android.view.View
//import android.view.Window
//import androidx.constraintlayout.widget.ConstraintLayout
//import com.lyj.customui.R
//import com.lyj.customui.dialog.extension.getDimen
//
//class PermissionDialog(
//    private val viewModel: PermissionDialogViewModel
//) :
//    Dialog(viewModel.context), View.OnClickListener {
//    private val horizontalMargin by lazy { context.getDimen(R.dimen.permission_dialog_container_horizontal_margin)}
//    private val verticalMargin by lazy { context.getDimen(R.dimen.permission_dialog_container_vertical_margin)}
//
//    private val container : ConstraintLayout by lazy { findViewById(R.id.permissionDialogContainer) }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        setContentView(R.layout.permission_dialog)
//        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        setBackgroundSize(horizontalMargin,verticalMargin)
//        requestPermission(viewModel)
//    }
//
//    private fun requestPermission(viewModel: PermissionDialogViewModel) = viewModel.requestPermission()
//
//    private fun setBackgroundSize(horizontalMargin : Float, verticalMargin : Float){
//        container.layoutParams.apply {
//            width = (viewModel.pxWidth - horizontalMargin).toInt()
//            height = (viewModel.pxHeight - verticalMargin).toInt()
//        }
//    }
//
//    override fun onClick(v: View) {}
//}
//
//data class Permission(
//    val permissionId : Int,
//    val isOptional : Boolean = false
//)