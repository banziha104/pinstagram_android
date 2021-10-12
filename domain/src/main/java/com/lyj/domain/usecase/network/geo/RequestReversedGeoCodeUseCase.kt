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

    private val regex = arrayOf(Regex("^*[시군]$"), Regex("^*[읍면구]$"), Regex("^*[동리]$"))
    fun execute(lat: Double, lng: Double): Single<String?> =
        repository
            .getReverseGeocoding("$lat,$lng")
            .subscribeOn(Schedulers.io())
            .map<String?> { response ->
                val data = response.data
                if (response.isOk && data != null && data.isNotEmpty()) {
                    data
                        .mapNotNull { it.address }
                        .filter { address ->
                            regex.any { regex -> regex.find(address) != null }
                        }
                        .sortedWith { a, b ->
                            regex.indexOfFirst { it.find(a) != null } - regex.indexOfFirst { it.find(b) != null }
                        }
                        .joinToString(" ")
                } else {
                    null
                }
            }
}