package com.lyj.domain.usecase.android.location

import android.app.Activity
import android.location.Location
import com.lyj.domain.repository.android.LocationRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    fun execute(activity: Activity): Single<Location>? =
        locationRepository
            .getUserLocationOnce(activity)?.subscribeOn(Schedulers.trampoline())
}