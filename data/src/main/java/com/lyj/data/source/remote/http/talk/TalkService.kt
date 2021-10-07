package com.lyj.data.source.remote.http.talk

import com.lyj.data.source.remote.socket.TalkMessage
import com.lyj.data.source.local.temp.base.ApiResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface TalkService {
    @GET("talk/getAllMessage")
    fun getAllMessage() : Single<ApiResponse<List<TalkMessage>>>
}