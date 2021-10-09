package com.lyj.data.source.remote.entity.event

import com.lyj.domain.model.network.event.EventModel

data class EventRetreiveResponse(
    override val contentsId: Long,
    override val title: String,
    override val description: String,
    override val fullAddress: String,
    override val picture: String,
    override val lat: Double,
    override val lng: Double,
) : EventModel
