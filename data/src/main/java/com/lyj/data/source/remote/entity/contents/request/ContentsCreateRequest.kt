package com.lyj.data.source.remote.entity.contents.request



data class ContentsCreateRequest(
    val title: String,
    val description: String,
    val picture: String,
    val tag: String,
    val lat: Double,
    val lng: Double
)