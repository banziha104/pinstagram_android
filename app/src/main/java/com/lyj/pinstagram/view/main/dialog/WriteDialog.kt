package com.lyj.pinstagram.view.main.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import com.lyj.pinstagram.R

class WriteDialog(private val viewModel: WriteDialogViewModel) : Dialog(viewModel.context),
    View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_write)

    }

    private fun setBackgroundSize(horizontalMargin: Float, verticalMargin: Float) {
//        findViewById<Consta>().layoutParams.apply {
//            width = (viewModel.pxWidth - horizontalMargin).toInt()
//            height = (viewModel.pxHeight - verticalMargin).toInt()
//        }
    }

    override fun onClick(v: View) {
        dismiss()
    }
}