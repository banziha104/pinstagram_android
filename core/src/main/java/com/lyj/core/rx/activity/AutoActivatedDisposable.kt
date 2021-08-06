package com.lyj.core.rx.activity

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.iyeongjoon.nicname.core.rx.DisposableAddable
import com.lyj.core.extension.simpleTag
import com.lyj.core.extension.testTag
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

typealias DisposableActive = () -> Disposable
// 라이프사이클 오너에 등록하는 익스텐션
class AutoActivatedDisposable(
        private val lifecycleOwner: LifecycleOwner
)
    : LifecycleObserver,DisposableAddable {

    private val list : MutableList<DisposableActive> = mutableListOf()
    private val compositionDisposable = CompositeDisposable()
    override fun add(disposable: Disposable?) {
        if (disposable != null) {
            list.add { disposable }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activate() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume(){
        list.map {
            Log.d(simpleTag,"시작 합니다")
            it.invoke()
        }.forEach { compositionDisposable.add(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun deactivate() {
        compositionDisposable.dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachSelf() {
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}
