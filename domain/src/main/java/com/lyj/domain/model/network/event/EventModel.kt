package com.lyj.domain.model.network.event

interface EventModel {
    val contentsId: Long
    val title: String
    val description: String
    val fullAddress: String
    val picture: String
    val lat: Double
    val lng: Double
}