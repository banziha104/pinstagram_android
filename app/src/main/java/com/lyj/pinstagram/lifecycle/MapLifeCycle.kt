package com.lyj.pinstagram.lifecycle

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.maps.MapView

class MapLifeCycle (
    lifecycleOwner: Fragment,
    private val map : MapView) : LifecycleObserver{

    init{
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        map.onResume()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){
        map.onPause()
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        map.onDestroy()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        map.onStop()
    }
}