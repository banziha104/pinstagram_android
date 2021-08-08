package com.lyj.pinstagram.location.protocol

import android.app.Activity
import android.location.Location
import io.reactivex.rxjava3.core.Single

interface OnceLocationGetter {
    fun getUserLocationOnce(activity: Activity): Single<Location>?
}