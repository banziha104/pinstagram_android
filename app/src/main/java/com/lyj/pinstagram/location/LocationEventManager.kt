package com.lyj.pinstagram.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.lyj.core.permission.PermissionChecker
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject


@SuppressLint("MissingPermission") // 현재 권한 체크는 하였지만, Lint 쪽에서 파악이 안되어서 Warning 발생, 이에 MissingPermission 달아줌
class LocationEventManager(
    context: Context,
    private val permissionChecker: PermissionChecker
) : OneTimeLocationGetter, ContinuosLocationGetter {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val subject: BehaviorSubject<Location> = BehaviorSubject.create()
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationListener: LocationListener = LocationListener { location ->
        subject.onNext(location)
    }

    override fun getSingle(activity: Activity): Single<Location>? =
        permissionChecker.checkAndRequestPermision(activity, permissions).flatMap {
            if (it) {
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
                Single.create { emitter ->
                    val last = subject.observeOn(Schedulers.newThread()).blockingLast()
                    emitter.onSuccess(last)
                    stopUpdateLocation()
                }
            } else {
                null
            }
        }

    override fun getLocationObserver(activity: Activity): Observable<Location>? {
        if (permissionChecker.checkAndRequestPermision(activity, permissions).blockingGet()) {
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