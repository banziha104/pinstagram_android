package com.lyj.data.source.remote.http.geo

import com.lyj.data.source.local.temp.base.ApiResponse
import com.lyj.data.source.local.temp.network.geo.geocoding.GeoResponse
import com.lyj.data.source.local.temp.network.geo.reverse.ReversedGeoResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GeometryService {
    @GET("geometry/")
    fun getReverseGeocoding(
        @Query("latlng") latLng : String,
    ) : Single<ApiResponse<ReversedGeoResponse>>

    @GET("geometry/geo")
    fun getGeocoding(
        @Query("address") address : String,
    ) : Single<ApiResponse<GeoResponse>>

}