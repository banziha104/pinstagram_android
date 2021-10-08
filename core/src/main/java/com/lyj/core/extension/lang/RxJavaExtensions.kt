package com.lyj.core.extension.lang

import com.lyj.core.rx.DisposableAddable
import com.lyj.core.rx.DisposableSubscription
import com.lyj.core.rx.DisposableFunctionAddable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

// 자동 제거 등록
operator fun DisposableAddable.plusAssign(disposable: Disposable?) = this.add(disposable)

operator fun DisposableFunctionAddable.plusAssign(disposable: DisposableSubscription) = this.add(disposable)

fun <T> Observable<T>.applyScheduler(subscribeOn : SchedulerType? = null, observeOn : SchedulerType? = null): Observable<T>{
    if(subscribeOn != null){
        this.subscribeOn(subscribeOn.scheduler)
    }
    if (observeOn != null){
        this.observeOn(observeOn.scheduler)
    }
    return this
}

fun <T> Single<T>.applyScheduler(subscribeOn : SchedulerType? = null, observeOn : SchedulerType? = null): Single<T>{
    if(subscribeOn != null){
        this.subscribeOn(subscribeOn.scheduler)
    }
    if (observeOn != null){
        this.observeOn(observeOn.scheduler)
    }
    return this
}

fun <T> Flowable<T>.applyScheduler(subscribeOn : SchedulerType? = null, observeOn : SchedulerType? = null): Flowable<T>{
    if(subscribeOn != null){
        this.subscribeOn(subscribeOn.scheduler)
    }
    if (observeOn != null){
        this.observeOn(observeOn.scheduler)
    }
    return this
}

fun <T> Maybe<T>.applyScheduler(subscribeOn : SchedulerType? = null, observeOn : SchedulerType? = null): Maybe<T> {
    if(subscribeOn != null){
        this.subscribeOn(subscribeOn.scheduler)
    }
    if (observeOn != null){
        this.observeOn(observeOn.scheduler)
    }
    return this
}
fun Completable.applyScheduler(subscribeOn : SchedulerType? = null, observeOn : SchedulerType? = null): Completable {
    if(subscribeOn != null){
        this.subscribeOn(subscribeOn.scheduler)
    }
    if (observeOn != null){
        this.observeOn(observeOn.scheduler)
    }
    return this
}


enum class SchedulerType(
    val scheduler: Scheduler
){
    MAIN(AndroidSchedulers.mainThread()),
    IO(Schedulers.io()),
    NEW(Schedulers.newThread()),
    TRAMPOLIN(Schedulers.trampoline()),
    COMPUTATION(Schedulers.computation())
}