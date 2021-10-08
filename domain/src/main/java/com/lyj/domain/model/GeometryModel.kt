package com.lyj.domain.model

data class ReversedGeoModel(
    val longName : String
)

data class GeoModel(
    val latitude : Double,
    val longitude : Double,
    val province : String
)