package com.lyj.pinstagram.location

import android.app.Activity
import android.location.Location
import io.reactivex.rxjava3.core.Observable

interface ContinuosLocationGetter {
    fun getLocationObserver(activity: Activity): Observable<Location>?
    fun stopUpdateLocation()
}