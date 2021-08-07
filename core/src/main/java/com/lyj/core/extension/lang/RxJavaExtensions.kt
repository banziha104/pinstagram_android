package com.lyj.core.extension.lang

import com.iyeongjoon.nicname.core.rx.DisposableAddable
import com.iyeongjoon.nicname.core.rx.DisposableFunction
import com.iyeongjoon.nicname.core.rx.DisposableFunctionAddable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

// 자동 제거 등록
operator fun DisposableAddable.plusAssign(disposable: Disposable?) = this.add(disposable)

operator fun DisposableFunctionAddable.plusAssign(disposable: DisposableFunction) = this.add(disposable)


fun runOnIoScheduler(func: () -> Unit): Disposable
        = Completable.fromCallable(func).subscribeOn(Schedulers.io()).subscribe()
