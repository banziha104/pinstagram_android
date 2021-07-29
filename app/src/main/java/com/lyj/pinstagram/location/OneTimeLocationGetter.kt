package com.lyj.pinstagram.location

import android.app.Activity
import android.location.Location
import io.reactivex.rxjava3.core.Single

interface OneTimeLocationGetter {
    fun getSingle(activity: Activity): Single<Location>?
}