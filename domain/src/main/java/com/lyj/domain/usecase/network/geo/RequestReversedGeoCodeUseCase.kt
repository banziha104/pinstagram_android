package com.lyj.domain.usecase.network.geo


import com.lyj.domain.repository.network.GeometryRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestReversedGeoCodeUseCase @Inject constructor(
    private val repository: GeometryRepository
) {
    fun execute(lat: Double, lng: Double): Single<String?> =
        repository
            .getReverseGeocoding("$lat,$lng")
            .subscribeOn(Schedulers.io())
            .map<String?> { response ->
                val data = response.data
                if (response.isOk && data != null && data.isNotEmpty()) {
                    val city =
                        data.firstOrNull { it.address?.endsWith("시") ?: false }?.address ?: ""
                    val province =
                        data.firstOrNull { it.address?.endsWith("시") ?: false }?.address ?: ""
                    val village =
                        data.firstOrNull { it?.address?.endsWith("동") ?: false }?.address ?: ""
                    "$city $province $village"
                } else {
                    null
                }
            }
}