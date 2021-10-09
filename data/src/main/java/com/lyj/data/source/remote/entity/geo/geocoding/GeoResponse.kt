package com.lyj.data.source.remote.entity.geo.geocoding

import com.lyj.domain.model.network.geo.GeoModel

data class GeoResponse(
    override val latitude : Double,
    override val longitude : Double,
    override val province : String
) : GeoModel