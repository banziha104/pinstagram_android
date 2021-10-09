package com.lyj.domain.usecase.network.geo


import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.geo.ReversedGeoModel
import com.lyj.domain.repository.network.GeometryRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestReversedGeoCodeUseCase @Inject constructor(
    private val repository: GeometryRepository
) {
    fun execute(latLng: String): Single<ApiModel<List<ReversedGeoModel>>> =
        repository.getReverseGeocoding(latLng).subscribeOn(Schedulers.io())
}