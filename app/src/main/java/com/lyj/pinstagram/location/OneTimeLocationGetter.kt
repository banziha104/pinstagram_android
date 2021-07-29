package com.lyj.pinstagram.location

import android.app.Activity
import android.location.Location
import io.reactivex.rxjava3.core.Single

interface OneTimeLocationGetter {
    fun getUserLocationOnce(activity: Activity): Single<Location>?
}