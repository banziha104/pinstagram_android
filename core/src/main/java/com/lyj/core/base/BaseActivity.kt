package com.lyj.core.base

import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.lyj.core.extension.android.plusAssign
import com.lyj.core.rx.activity.AutoActivatedDisposable
import com.lyj.core.rx.activity.AutoClearedDisposable

abstract class BaseActivity< VIEW_MODEL : ViewModel, DATA_BINDING : ViewDataBinding>(private val layoutId : Int) : AppCompatActivity() {

    protected val disposables  by lazy { AutoClearedDisposable(this) }
    protected val viewDisposables by lazy { AutoActivatedDisposable(lifecycleOwner = this) }
    protected lateinit var binding: DATA_BINDING
    protected abstract val viewModel: VIEW_MODEL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindLifecycle()
        bindDataBinding()
    }

    private fun bindLifecycle(){
        lifecycle += disposables
        lifecycle += viewDisposables
    }

    private fun bindDataBinding(){
        binding = DataBindingUtil.setContentView(this, layoutId)
    }
}