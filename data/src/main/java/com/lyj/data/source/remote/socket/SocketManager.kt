package com.lyj.data.source.remote.socket

import androidx.lifecycle.Lifecycle
import com.google.gson.Gson
import com.lyj.data.source.remote.http.ApiBase
import com.lyj.domain.model.network.talk.TalkModel
import com.lyj.domain.repository.network.SocketContract
import com.lyj.domain.repository.network.SocketFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import java.net.URI

class SocketManager(
    lifecycle: Lifecycle?,
) : SocketContract {

    private val sayObserver = BehaviorSubject.create<TalkModel>()

    private val gson: Gson by lazy { Gson() }
    private val socket: Socket by lazy {
        val opts = IO.Options.builder()
            .setPath("/talk/socket")
            .setTransports(arrayOf(WebSocket.NAME))
            .build()
        IO.socket(URI.create(ApiBase.BASE_URL), opts)
    }

    init {
        if (lifecycle != null) SocketLifeCycle(lifecycle, this)
    }

    override fun connect(): Completable = Completable.create { emitter ->
        socket.on("say") {
            sayObserver.onNext(gson.fromJson(it[0].toString(), TalkModel::class.java))
        }
        socket.on(Socket.EVENT_CONNECT) {
            emitter.onComplete()
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) {
            emitter.onError(RuntimeException(it.joinToString(",")))
        }
        socket.connect()
    }

    override fun sendMessage(talkMessage: TalkModel) {
        socket.emit("say", gson.toJson(talkMessage))
    }

    override fun disconnect(): Completable = Completable.create { emitter ->
        val socket = socket.close()
        if (socket.connected()) {
            emitter.onError(RuntimeException("Connection is alive"))
        } else {
            emitter.onComplete()
        }
    }

    override fun getSayObserver(): Observable<TalkModel> = sayObserver

    object Factory : SocketFactory {
        override fun createSocket(lifecycle: Lifecycle?): SocketContract = SocketManager(lifecycle)
    }
}