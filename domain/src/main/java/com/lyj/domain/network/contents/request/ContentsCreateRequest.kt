package com.lyj.domain.network.contents.request


class ContentsCreateRequest(
    val title: String,
    val description: String,
    val picture: String,
    val tag: String,
    val lat: Double,
    val lng: Double
)