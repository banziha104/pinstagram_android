package com.lyj.core.rx

import androidx.lifecycle.LifecycleObserver
import io.reactivex.rxjava3.disposables.Disposable

typealias DisposableFunction = () -> Disposable?

interface DisposableAddable : LifecycleObserver {
    fun add(disposable: Disposable?)
}

class DisposableSubscription(
    val lifecycle: DisposableLifecycle,
    val generator: DisposableFunction
)

interface DisposableFunctionAddable : LifecycleObserver {
    fun add(disposableFunction: DisposableSubscription?)
}

