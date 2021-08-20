package com.lyj.core.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.extension.android.plusAssign
import com.lyj.core.rx.AutoActivatedDisposable
import com.lyj.core.rx.AutoClearedDisposable

abstract class BaseActivity< VIEW_MODEL : ViewModel,  VIEW_BINDING : ViewBinding>(private val layoutId : Int, private val factory : (LayoutInflater) -> VIEW_BINDING) : AppCompatActivity() {

    protected val disposables  by lazy { AutoClearedDisposable(this) }
    protected val viewDisposables by lazy { AutoActivatedDisposable(lifecycleOwner = this) }
    protected val binding: VIEW_BINDING by lazy { factory(layoutInflater) }
    protected abstract val viewModel: VIEW_MODEL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindLifecycle()
    }

    private fun bindLifecycle(){
        lifecycle += disposables
        lifecycle += viewDisposables
    }

}