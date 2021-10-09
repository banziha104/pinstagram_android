package com.lyj.data.repository.network

import com.lyj.data.mapper.ApiMapper
import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.data.source.remote.entity.geo.geocoding.GeoResponse
import com.lyj.data.source.remote.entity.geo.reverse.AddressComponentsItem
import com.lyj.data.source.remote.http.geo.GeometryService
import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.geo.GeoModel
import com.lyj.domain.model.network.geo.ReversedGeoModel
import com.lyj.domain.repository.network.GeometryRepository
import io.reactivex.rxjava3.core.Single

class GeometryRepositoryImpl(
    private val service: GeometryService
) : GeometryRepository {
    override fun getReverseGeocoding(latLng: String): Single<ApiModel<List<ReversedGeoModel>>> =
        service
            .getReverseGeocoding(latLng)
            .map { response ->
                val data = response.data?.results?.get(0)?.addressComponents
                    ?: listOf()
                response.copyWith<List<ReversedGeoModel>>(data.mapNotNull { it })
            }.map(ApiMapper::toModel)


    override fun getGeocoding(address: String): Single<ApiModel<GeoModel>> =
        service.getGeocoding(address).map(ApiMapper::toModel)
}