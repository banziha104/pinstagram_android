package com.lyj.data.source.remote.http.event

import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.data.source.remote.entity.event.EventRetreiveResponse
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