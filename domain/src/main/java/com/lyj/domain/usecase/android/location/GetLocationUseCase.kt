package com.lyj.domain.usecase.android.location

import android.app.Activity
import android.location.Location
import com.lyj.core.extension.lang.SchedulerType
import com.lyj.core.extension.lang.applyScheduler
import com.lyj.domain.repository.android.LocationRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    fun execute(activity: Activity): Single<Location>? =
        locationRepository
            .getUserLocationOnce(activity)?.applyScheduler(subscribeOn = SchedulerType.TRAMPOLIN)
}