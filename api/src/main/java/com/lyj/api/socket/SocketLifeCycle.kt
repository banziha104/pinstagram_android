package com.lyj.api.socket

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class SocketLifeCycle(
    lifecycle: Lifecycle,
    private val socketContract: SocketContract
) : LifecycleObserver {

    init{
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        socketContract.disconnect()
    }
}