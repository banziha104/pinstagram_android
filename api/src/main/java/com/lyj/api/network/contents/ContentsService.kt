package com.lyj.api.network.contents

import com.lyj.domain.base.ApiResponse
import com.lyj.domain.network.contents.request.ContentsCreateRequest
import com.lyj.domain.network.contents.response.ContentsRetrieveResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ContentsService {
    @GET("contents/")
    fun getByLocation(
        @Query("latlng") latlng : String,
        @Query("limit") limit : Int = 1000
    ) : Single<ApiResponse<List<ContentsRetrieveResponse>>>

    @GET("contents/{id}")
    suspend fun getById(
        @Path("id") id : Long
    ) : ApiResponse<ContentsRetrieveResponse>

    @POST("contents/")
    fun createContents(
        @Header("Authorization") authorization : String,
        @Body request: ContentsCreateRequest
    ) : Single<ApiResponse<Long>>

}