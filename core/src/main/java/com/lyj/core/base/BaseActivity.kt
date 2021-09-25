package com.lyj.core.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.extension.android.plusAssign
import com.lyj.core.rx.AutoDisposableController
import com.lyj.core.rx.DisposableScopes

abstract class BaseActivity< VIEW_MODEL : ViewModel,  VIEW_BINDING : ViewBinding>(private val layoutId : Int, private val factory : (LayoutInflater) -> VIEW_BINDING) : AppCompatActivity() {

    protected val binding: VIEW_BINDING by lazy { factory(layoutInflater) }
    protected val scopes : DisposableScopes by lazy { DisposableScopes(disposableController) }
    internal val disposableController by lazy { AutoDisposableController(lifecycleOwner = this) }

    protected abstract val viewModel: VIEW_MODEL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindLifecycle()
    }

    private fun bindLifecycle(){
        lifecycle += disposableController
    }
}