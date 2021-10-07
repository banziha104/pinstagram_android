package com.lyj.data.source.remote.socket

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.lyj.core.extension.lang.socketTag

class SocketLifeCycle(
    lifecycle: Lifecycle,
    private val socketContract: SocketContract
) : LifecycleObserver {

    init{
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        Log.d(socketTag,"DISCONNECT SOCKET ")
        socketContract.disconnect()
    }
}