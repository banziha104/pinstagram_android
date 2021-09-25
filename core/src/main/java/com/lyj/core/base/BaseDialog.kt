package com.lyj.core.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.extension.android.plusAssign
import com.lyj.core.rx.AutoDisposableController
import com.lyj.core.rx.DisposableScopes

abstract class BaseDialog< VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding>(
    private val factory: (LayoutInflater,ViewGroup?,Bundle?) -> VIEW_BINDING
) : DialogFragment() {

    protected val scopes : DisposableScopes by lazy { DisposableScopes(disposableController) }
    internal val disposableController by lazy { AutoDisposableController(lifecycleOwner = this) }

    protected lateinit var binding: VIEW_BINDING
    protected abstract val viewModel: VIEW_MODEL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bindLifecycle()
        binding = factory(inflater, container,savedInstanceState)
        return binding.root
    }

    private fun bindLifecycle() {
        lifecycle += disposableController
    }
}
