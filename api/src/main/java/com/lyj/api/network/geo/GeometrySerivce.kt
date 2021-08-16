package com.lyj.api.network.geo

import com.lyj.domain.base.ApiResponse
import com.lyj.domain.network.geo.geocoding.GeoResponse
import com.lyj.domain.network.geo.reverse.ReversedGeoResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GeometrySerivce {
    @GET("geometry/")
    fun getReverseGeocoding(
        @Query("latlng") latLng : String,
    ) : Single<ApiResponse<ReversedGeoResponse>>

    @GET("geometry/geo")
    fun getGeocoding(
        @Query("address") address : String,
    ) : Single<ApiResponse<GeoResponse>>

}