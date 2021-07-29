package com.lyj.pinstagram.view.main.fragments.map

import android.app.Activity
import android.location.LocationManager
import android.location.LocationProvider
import androidx.lifecycle.ViewModel
import com.lyj.pinstagram.location.LocationEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapFragmentViewModel @Inject constructor(
    val locationProvider: LocationEventManager
) : ViewModel() {
}