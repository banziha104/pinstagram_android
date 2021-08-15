package com.lyj.core.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.extension.android.plusAssign
import com.lyj.core.rx.AutoActivatedDisposable
import com.lyj.core.rx.AutoClearedDisposable

abstract class BaseDialog<VIEW_BINDING : ViewBinding, VIEW_MODEL : ViewModel>(
    private val factory: (LayoutInflater,ViewGroup?,Bundle?) -> VIEW_BINDING
) : DialogFragment() {
    protected lateinit var binding: VIEW_BINDING
    protected val disposables by lazy { AutoClearedDisposable(this) }
    protected val viewDisposables by lazy { AutoActivatedDisposable(lifecycleOwner = this) }
    protected abstract val viewModel: VIEW_MODEL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindLifecycle()
        binding = factory(inflater, container,savedInstanceState)
        return binding.root
    }

    private fun bindLifecycle() {
        lifecycle += disposables
        lifecycle += viewDisposables
    }
}