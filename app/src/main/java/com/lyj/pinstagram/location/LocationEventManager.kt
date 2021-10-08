package com.lyj.pinstagram.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.lyj.core.permission.PermissionChecker
import com.lyj.pinstagram.location.protocol.ContinuousLocationGetter
import com.lyj.pinstagram.location.protocol.OnceLocationGetter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.SingleSubject


@SuppressLint("MissingPermission") // 현재 권한 체크는 하였지만, Lint 쪽에서 파악이 안되어서 Warning 발생, 이에 MissingPermission 달아줌
class LocationEventManager(
    context: Context,
    private val permissionChecker: PermissionChecker
) : OnceLocationGetter, ContinuousLocationGetter {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val subject: BehaviorSubject<Location> = BehaviorSubject.create()
    private val singleSubject : SingleSubject<Location> = SingleSubject.create()
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationListener: LocationListener = LocationListener { location ->
        subject.onNext(location)
        singleSubject.onSuccess(location)
    }

    override fun getUserLocationOnce(activity: Activity): Single<Location>?{
        if (permissionChecker.checkAndRequestPermission(activity, permissions).blockingGet()) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                1.0f,
                locationListener
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,
                1.0f,
                locationListener
            )
            return singleSubject.doOnSuccess { stopUpdateLocation() }
        } else {
            return null
        }
    }

    override fun getLocationObserver(activity: Activity): Observable<Location>? {
        if (permissionChecker.checkAndRequestPermission(activity, permissions).blockingGet()) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                1.0f,
                locationListener
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,
                1.0f,
                locationListener
            )
            return subject
        } else {
            return null
        }
    }

    override fun stopUpdateLocation() {
        locationManager.removeUpdates(locationListener)
    }
}