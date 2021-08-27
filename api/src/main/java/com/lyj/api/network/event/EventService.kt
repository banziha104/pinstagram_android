package com.lyj.api.network.event

import com.lyj.domain.base.ApiResponse
import com.lyj.domain.network.event.EventRetreiveResponse
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {
    @GET("event/{id}")
    suspend fun getById(
        @Path("id") id : Long
    ) : ApiResponse<EventRetreiveResponse>

    @GET("event/")
    suspend fun getByLocation(
        @Query("latlng") latlng : String,
        @Query("limit") limit : Int = 1000
    ) : ApiResponse<List<EventRetreiveResponse>>
}