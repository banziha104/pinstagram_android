package com.lyj.data.source.remote.http.talk

import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.domain.model.network.talk.TalkModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface TalkService {
    @GET("talk/getAllMessage")
    fun getAllMessage() : Single<ApiResponse<List<TalkModel>>>
}