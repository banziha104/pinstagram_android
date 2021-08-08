package com.lyj.pinstagram.location.protocol

import android.app.Activity
import android.location.Location
import io.reactivex.rxjava3.core.Observable

interface ContinuousLocationGetter {
    fun getLocationObserver(activity: Activity): Observable<Location>?
    fun stopUpdateLocation()
}