package com.lyj.domain.usecase.network.geo

import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.geo.GeoModel
import com.lyj.domain.repository.network.GeometryRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestGeoCodeUseCase @Inject constructor(
    private val repository: GeometryRepository
) {
    fun execute(address: String): Single<ApiModel<GeoModel>> = repository.getGeocoding(address).subscribeOn(Schedulers.io())
}