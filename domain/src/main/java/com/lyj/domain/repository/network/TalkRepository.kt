package com.lyj.domain.repository.network

import androidx.lifecycle.Lifecycle
import com.lyj.domain.model.TalkModel
import com.lyj.domain.model.network.ApiModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface TalkRepository : SocketFactory {
    fun getAllMessage() : Single<ApiModel<List<TalkModel>>>
}

interface SocketFactory{
    fun createSocket(lifecycle: Lifecycle?) : SocketContract
}

interface SocketContract {
    fun connect(): Completable
    fun sendMessage(talkMessage: TalkModel)
    fun disconnect(): Completable
    fun getSayObserver(): Observable<TalkModel>
}