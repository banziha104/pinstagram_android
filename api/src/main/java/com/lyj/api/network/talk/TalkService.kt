package com.lyj.api.network.talk

import com.lyj.api.socket.TalkMessage
import com.lyj.domain.base.ApiResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface TalkService {
    @GET("talk/getAllMessage")
    fun getAllMessage() : Single<ApiResponse<List<TalkMessage>>>
}