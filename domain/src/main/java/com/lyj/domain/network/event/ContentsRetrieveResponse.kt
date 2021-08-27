package com.lyj.domain.network.event

data class EventRetreiveResponse(
    val contentsId: Long,
    val title: String,
    val description: String,
    val fullAddress: String,
    val picture: String,
    val lat: Double,
    val lng: Double,
)
