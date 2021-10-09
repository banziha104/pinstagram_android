package com.lyj.data.repository.android

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.lyj.domain.repository.android.LocationRepository
import com.lyj.domain.repository.android.PermissionRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.SingleSubject

class LocationRepositoryImpl(
    context: Context,
    private val permissionRepository: PermissionRepository
) : LocationRepository {


    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val singleSubject: SingleSubject<Location> = SingleSubject.create()
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationListener: LocationListener = LocationListener { location ->
        singleSubject.onSuccess(location)
    }

    override fun getUserLocationOnce(activity: Activity): Single<Location>? {
        if (permissionRepository.checkAndRequestPermission(activity, permissions).blockingGet()) {
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

    private fun stopUpdateLocation() {
        locationManager.removeUpdates(locationListener)
    }
}