package com.lyj.data.source.remote.socket

import com.lyj.domain.model.network.auth.JwtModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface SocketContract {
    fun connect(): Completable
    fun sendMessage(talkMessage: TalkMessage)
    fun disconnect(): Completable
    fun getSayObserver(): Observable<TalkMessage>
}


data class TalkMessage(
    val name: String,
    val text: String,
    val userId: Long,
    val id: Long? = null
) {
    companion object {
        fun withAuth(jwtAuthData: JwtModel, text: String): TalkMessage? =
            if (jwtAuthData.isValidated) TalkMessage(
                jwtAuthData.name!!,
                text,
                jwtAuthData.id!!.toLong()
            ) else {
                null
            }
    }
}