package com.lyj.api.socket

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.gson.Gson
import com.lyj.core.extension.socketTag
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import java.lang.RuntimeException
import java.net.URI

class SocketManager(
    lifecycle: Lifecycle,
    private val sayListener: (TalkMessage) -> Void
) : SocketContract{

    private val gson : Gson by lazy { Gson() }
    private val socket : Socket by lazy {
        val opts = IO.Options.builder()
            .setPath("/talk/socket")
            .setTransports(arrayOf(WebSocket.NAME))
            .build()
        IO.socket(URI.create("https://www.coguri.shop"), opts)
    }

    init {
        SocketLifeCycle(lifecycle,this)
    }


    override fun connect(): Completable = Completable.create {  emitter ->
        socket.on("say") {
            sayListener(gson.fromJson(it[0].toString(),TalkMessage::class.java))
        }
        socket.on(Socket.EVENT_CONNECT) {
            emitter.onComplete()
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) {
            emitter.onError(RuntimeException(it.joinToString(",")))
        }
        socket.connect()
    }

    override fun sendMessage(talkMessage: TalkMessage) {
        socket.emit("say",gson.toJson(talkMessage))
    }

    override fun disconnect(): Completable = Completable.create{ emitter ->
        socket.on(Socket.EVENT_CONNECT) {
            emitter.onComplete()
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) {
            emitter.onError(RuntimeException(it.joinToString(",")))
        }
        socket.disconnect()
    }
}