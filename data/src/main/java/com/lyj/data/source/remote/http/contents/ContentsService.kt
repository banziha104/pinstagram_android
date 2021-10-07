package com.lyj.data.source.remote.http.contents

import com.lyj.data.source.local.temp.base.ApiResponse
import com.lyj.data.source.local.temp.network.contents.request.ContentsCreateRequest
import com.lyj.data.source.local.temp.network.contents.response.ContentsRetrieveResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ContentsService {

    @GET("contents/{id}")
    suspend fun getById(
        @Path("id") id : Long
    ) : ApiResponse<ContentsRetrieveResponse>

    @GET("contents/")
    fun getByLocation(
        @Query("latlng") latlng : String,
        @Query("limit") limit : Int = 1000
    ) : Single<ApiResponse<List<ContentsRetrieveResponse>>>

    @POST("contents/")
    fun createContents(
        @Header("Authorization") authorization : String,
        @Body request: ContentsCreateRequest
    ) : Single<ApiResponse<Long>>
}

