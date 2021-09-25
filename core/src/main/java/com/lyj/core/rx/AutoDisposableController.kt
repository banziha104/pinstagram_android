package com.lyj.core.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AutoDisposableController(
    private val lifecycleOwner: LifecycleOwner
) : LifecycleObserver, DisposableFunctionAddable {

    private val list: MutableList<DisposableSubscription> = mutableListOf()

    private val onPauseDisposable = CompositeDisposable()
    private val onStopDisposable = CompositeDisposable()
    private val onDestroyDisposable = CompositeDisposable()

    override fun add(disposableSubscription: DisposableSubscription?) {
        if (disposableSubscription != null){
            list.add(disposableSubscription)

            if (disposableSubscription.lifecycle.entryPoint.isRunImmediately){
                val disposable = disposableSubscription.generator.invoke()
                when(disposableSubscription.lifecycle.endPoint){
                    EndPoint.PAUSE -> onPauseDisposable.add(disposable)
                    EndPoint.STOP -> onStopDisposable.add(disposable)
                    EndPoint.DESTROY -> onDestroyDisposable.add(disposable)
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun any(owner : LifecycleOwner, event : Lifecycle.Event){
        when(event){
            Lifecycle.Event.ON_CREATE,Lifecycle.Event.ON_START,Lifecycle.Event.ON_RESUME -> {
                activate(event)
            }
            Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> {
                deactivate(event)
            }
            Lifecycle.Event.ON_DESTROY -> {
                deactivate(event)
                onDestroyDisposable.dispose()
                onPauseDisposable.dispose()
                onStopDisposable.dispose()
                lifecycleOwner.lifecycle.removeObserver(this)
            }

        }
    }

    private fun activate(event: Lifecycle.Event) {
        list
            .filter { it.lifecycle.entryPoint.lifecycle == event }
            .forEach {
                val disposable = it.generator.invoke()
                when(it.lifecycle.endPoint){
                    EndPoint.PAUSE -> onPauseDisposable.add(disposable)
                    EndPoint.STOP -> onStopDisposable.add(disposable)
                    EndPoint.DESTROY -> onDestroyDisposable.add(disposable)
                }
            }
    }

    private fun deactivate(event: Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_PAUSE -> onPauseDisposable.clear()
            Lifecycle.Event.ON_STOP -> onStopDisposable.clear()
            Lifecycle.Event.ON_DESTROY -> onDestroyDisposable.clear()
        }
    }
}


