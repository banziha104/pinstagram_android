package com.lyj.api.socket

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface SocketContract {
    fun connect() : Completable
    fun sendMessage(talkMessage: TalkMessage)
    fun disconnect() : Completable
}


data class TalkMessage(
    val name : String,
    val id : Long,
    val text : String? = null
)