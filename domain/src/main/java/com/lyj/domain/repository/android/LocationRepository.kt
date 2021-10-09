package com.lyj.domain.repository.android

import android.app.Activity
import android.location.Location
import io.reactivex.rxjava3.core.Single

interface LocationRepository {
    fun getUserLocationOnce(activity: Activity): Single<Location>?
}