package com.lyj.core.rx.fragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.fragment.app.Fragment
import com.iyeongjoon.nicname.core.rx.DisposableAddable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

// 라이프사이클에맞게 해제되는 익스텐션
class AutoClearedDisposable(
    private val lifecycleOwner: Fragment,
    private val alwaysClearOnStop: Boolean = true,
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()) : DisposableAddable {


    override fun add(disposable: Disposable?) {
        if (disposable!= null) {
            check(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))
            compositeDisposable.add(disposable)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun cleanUp() {
        if (!alwaysClearOnStop && !lifecycleOwner.isDetached) {
            return
        }
        compositeDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachSelf() {
        compositeDisposable.clear()
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}