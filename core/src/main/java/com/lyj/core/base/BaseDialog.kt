package com.lyj.core.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<VIEW_BINDING : ViewBinding>(
    viewModel: DialogViewModel,
    private val factory: (LayoutInflater) -> VIEW_BINDING
) : Dialog(viewModel.context) {
    protected lateinit var binding: VIEW_BINDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = factory(layoutInflater)
        setContentView(binding.root)
    }
}