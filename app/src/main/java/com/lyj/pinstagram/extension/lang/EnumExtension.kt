package com.lyj.pinstagram.extension.lang

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

fun <T : Enum<*>> T.observable() : Observable<T> = Observable.just(this)
fun <T : Enum<*>> T.single() : Single<T> = Single.just(this)