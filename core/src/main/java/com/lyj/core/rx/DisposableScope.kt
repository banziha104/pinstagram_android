package com.lyj.core.rx

import com.lyj.core.extension.lang.plusAssign

// 해당 라이프사이클과 AutoDisposableController를 통해 bind로 들어오는 Disposable 객체를 관리
class DisposableScope(
    val lifecycle: DisposableLifecycle,
    private val autoDisposableController: AutoDisposableController
) {
    fun bind(generator: DisposableFunction) {
        autoDisposableController += DisposableSubscription(lifecycle, generator)
    }
}

operator fun DisposableScope.plusAssign(generator: DisposableFunction) = bind(generator)


// 해당 라이프사이클이 가질 수 있는 모든 Scope를 조회할 수 있도록 하는 객체 ( 주로 종속된 컴포넌트의 라이프사이클을 받아와야하는 객체의 전달용, 예: Activity -> Adatper)
class DisposableScopes(
    private val controller: AutoDisposableController
) {
    private val map: Map<DisposableLifecycle, DisposableScope> =
        EntryPoint.values().map { entry ->
            EndPoint.values().map { end ->
                (entry at end) scopeWith controller
            }.toList()
        }.flatten()
            .groupBy { it.lifecycle }
            .entries
            .associate { it.key to it.value[0] }

    fun get(lifecycle: DisposableLifecycle): DisposableScope =
        map[lifecycle] ?: throw NullPointerException()

    val fromCreateToDestory : DisposableScope
    get() = map[EntryPoint.CREATE at EndPoint.DESTROY]!!

    val fromStartToStop : DisposableScope
        get() = map[EntryPoint.START at EndPoint.STOP]!!

    val fromResumeToPause : DisposableScope
        get() = map[EntryPoint.RESUME at EndPoint.PAUSE]!!
}

operator fun DisposableScopes.get(lifecycle: DisposableLifecycle): DisposableScope = get(lifecycle)
