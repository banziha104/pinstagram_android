package com.iyeongjoon.nicname.core.rx

import androidx.lifecycle.LifecycleObserver
import io.reactivex.rxjava3.disposables.Disposable

interface DisposableAddable : LifecycleObserver{
    fun add(disposable: Disposable)
}