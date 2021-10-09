package com.lyj.data.repository.network

import com.lyj.data.mapper.ApiMapper
import com.lyj.data.source.remote.entity.event.EventRetreiveResponse
import com.lyj.data.source.remote.http.event.EventService
import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.event.EventModel
import com.lyj.domain.repository.network.EventRepository

class EventRepositoryImpl(
    private val service: EventService
) : EventRepository {
    override suspend fun getById(id: Long): ApiModel<EventModel> =
        ApiMapper.toModel(service.getById(id))

    override suspend fun getByLocation(latlng: String, limit: Int): ApiModel<List<EventModel>> =
        ApiMapper.toModel(service.getByLocation(latlng, limit))
}