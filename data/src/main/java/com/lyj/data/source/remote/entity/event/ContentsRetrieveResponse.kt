package com.lyj.data.source.remote.entity.event

data class EventRetreiveResponse(
    val contentsId: Long,
    val title: String,
    val description: String,
    val fullAddress: String,
    val picture: String,
    val lat: Double,
    val lng: Double,
)
