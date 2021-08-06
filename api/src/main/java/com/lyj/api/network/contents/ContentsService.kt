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
        @Query("limit") limit : Int
    ) : Single<ApiResponse<List<ContentsRetrieveResponse>>>

    @GET("contents/{id}")
    fun getById(
        @Path("id") latlng : Long
    ) : Single<ApiResponse<ContentsRetrieveResponse>>

    @POST("contents/")
    fun createContents(
        @Header("Authorization") authorization : String,
        @Body request: ContentsCreateRequest
    ) : Single<ApiResponse<Long>>

}