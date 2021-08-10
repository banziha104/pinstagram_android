package com.lyj.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.extension.android.plusAssign
import com.lyj.core.rx.AutoActivatedDisposable
import com.lyj.core.rx.AutoClearedDisposable


abstract class BaseFragment<VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding>(
    private val layoutId: Int,
    private val factory: (LayoutInflater, ViewGroup?) -> VIEW_BINDING
) : Fragment() {

    protected val disposables by lazy { AutoClearedDisposable(this) }
    protected val viewDisposables by lazy { AutoActivatedDisposable(lifecycleOwner = this) }
    protected lateinit var binding: VIEW_BINDING
    protected abstract val viewModel: VIEW_MODEL


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindLifecycle()
        binding = factory(inflater, container)
        return binding.root
    }

    private fun bindLifecycle() {
        lifecycle += disposables
        lifecycle += viewDisposables
    }
}