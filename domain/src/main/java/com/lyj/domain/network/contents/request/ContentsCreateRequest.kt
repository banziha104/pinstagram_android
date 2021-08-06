package com.lyj.domain.network.contents.request

import com.lyj.domain.network.contents.ContentsTagType


data class ContentsCreateRequest(
    val title: String,
    val description: String,
    val picture: String,
    val tag: ContentsTagType,
    val lat: Double,
    val lng: Double
)