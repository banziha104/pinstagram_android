package com.lyj.pinstagram.view.main.fragments.map

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.lyj.core.base.BaseFragment
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.MapFragmentBinding
import com.lyj.pinstagram.view.main.fragments.talk.TalkFragment

class MapFragment : BaseFragment<MapFragmentViewModel,MapFragmentBinding>(R.layout.map_fragment), OnMapReadyCallback {

    companion object{
        val instance : MapFragment by lazy { MapFragment() }
    }

    override val viewModel: MapFragmentViewModel by viewModels()
    private lateinit var map: GoogleMap


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
//       val observer =  locationManager
//            .getLocationObserver()
//        if (observer != null){
//
//        }else{
//
//        }
//        viewDisposables += locationManager
//            .getLocationObserver()
//            .
    }

}