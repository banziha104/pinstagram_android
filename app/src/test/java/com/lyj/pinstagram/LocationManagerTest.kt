package com.lyj.pinstagram

import android.util.Log
import com.lyj.core.extension.testTag
import com.lyj.core.permission.PermissionManager
import com.lyj.pinstagram.location.LocationEventManager
import com.lyj.pinstagram.view.main.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocationManagerTest {

    private lateinit var locationManager: LocationEventManager

    @Before
    fun init() {
    }

    @Test
    fun `사용자_위치_한번_가져오기`() {
        var activity = Robolectric.setupActivity(MainActivity::class.java)
        locationManager = LocationEventManager(activity,PermissionManager(activity))
        locationManager
            .getUserLocationOnce(activity)
            ?.subscribe({
                Log.d(testTag, "location : $it")
            }, {
                it.printStackTrace()
            })
    }

}