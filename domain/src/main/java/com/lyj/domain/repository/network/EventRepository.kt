package com.lyj.domain.repository.network

import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.event.EventModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventRepository {
    suspend fun getById(id: Long): ApiModel<EventModel>
    suspend fun getByLocation(latlng: String, limit: Int = 1000): ApiModel<List<EventModel>>
}