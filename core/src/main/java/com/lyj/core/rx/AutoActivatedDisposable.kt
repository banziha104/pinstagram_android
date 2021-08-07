package com.lyj.core.rx

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.iyeongjoon.nicname.core.rx.DisposableFunction
import com.iyeongjoon.nicname.core.rx.DisposableFunctionAddable
import io.reactivex.rxjava3.disposables.CompositeDisposable

// 라이프사이클 오너에 등록하는 익스텐션
class AutoActivatedDisposable(
        private val lifecycleOwner: LifecycleOwner
)
    : LifecycleObserver, DisposableFunctionAddable {

    private val list : MutableList<DisposableFunction> = mutableListOf()
    private val compositionDisposable = CompositeDisposable()
    override fun add(disposable: DisposableFunction?) {
        if (disposable != null) list.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activate() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume(){
        list.map {
            it.invoke()
        }.forEach { compositionDisposable.add(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun deactivate() {
        compositionDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachSelf() {
        compositionDisposable.dispose()
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}
