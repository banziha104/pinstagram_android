package com.lyj.domain.repository.network

import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.geo.GeoModel
import com.lyj.domain.model.network.geo.ReversedGeoModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GeometryRepository {
    fun getReverseGeocoding(latLng: String): Single<ApiModel<List<ReversedGeoModel>>>
    fun getGeocoding(address: String): Single<ApiModel<GeoModel>>
}