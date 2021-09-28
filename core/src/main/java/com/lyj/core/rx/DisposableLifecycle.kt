
package com.lyj.core.rx

import androidx.lifecycle.Lifecycle


data class DisposableLifecycle(
    val entryPoint: EntryPoint,
    val endPoint: EndPoint,
)

infix fun DisposableLifecycle.scopeWith(controller: AutoDisposableController) : DisposableScope = DisposableScope(this,controller)

infix fun EntryPoint.at(endPoint: EndPoint) = DisposableLifecycle(this, endPoint)

enum class EntryPoint(
    val lifecycle: Lifecycle.Event?,
    var isRunImmediately : Boolean = false
) {
    CREATE(Lifecycle.Event.ON_CREATE),
    START(Lifecycle.Event.ON_START),
    RESUME(Lifecycle.Event.ON_RESUME),
    IMMEDIATELY(null,true);

    // 해당 이벤트 (예 : OnCrate) 보다 늦게 호출되어 실행이 안되는 경우, 호출 시 즉시 실행
    fun runImmediately() : EntryPoint{
        this.isRunImmediately = true
        return this
    }
}

enum class EndPoint(
    val lifecycle: Lifecycle.Event
) {
    PAUSE(Lifecycle.Event.ON_PAUSE),
    STOP(Lifecycle.Event.ON_STOP),
    DESTROY(Lifecycle.Event.ON_DESTROY)
}