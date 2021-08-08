package com.lyj.pinstagram.view.main.fragments.map

import androidx.lifecycle.ViewModel
import com.lyj.pinstagram.location.LocationEventManager
import com.lyj.pinstagram.location.protocol.OnceLocationGetter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapFragmentViewModel @Inject constructor(
    locationProvider: LocationEventManager
) : ViewModel(), OnceLocationGetter by locationProvider {

}