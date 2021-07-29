package com.lyj.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.lyj.core.extension.android.plusAssign
import com.lyj.core.rx.fragment.AutoActivatedDisposable
import com.lyj.core.rx.fragment.AutoClearedDisposable

abstract class BaseFragment< VIEW_MODEL : ViewModel, DATA_BINDING : ViewDataBinding>(private val layoutId : Int) : Fragment() {

    protected val disposables  by lazy { AutoClearedDisposable(this) }
    protected val viewDisposables by lazy { AutoActivatedDisposable(lifecycleOwner = this) }
    protected lateinit var binding: DATA_BINDING
    protected abstract val viewModel: VIEW_MODEL


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindLifecycle()
        binding = DataBindingUtil.inflate(inflater,layoutId,container,false)
        return binding.root
    }
    private fun bindLifecycle(){
        lifecycle += disposables
        lifecycle += viewDisposables
    }
}