package com.lyj.pinstagram.view.main.fragments.map

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.lyj.core.base.BaseFragment
import com.lyj.core.extension.lang.plusAssign
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.MapFragmentBinding
import com.lyj.pinstagram.lifecycle.MapLifeCycle
import com.lyj.pinstagram.view.main.fragments.talk.TalkFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment private constructor() :
    BaseFragment<MapFragmentViewModel, MapFragmentBinding>(R.layout.map_fragment),
    OnMapReadyCallback {

    companion object {
        val instance: MapFragment by lazy { MapFragment() }
    }

    override val viewModel: MapFragmentViewModel by viewModels()

    private val zoomLevel = 16.5f

    private lateinit var map: GoogleMap


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            mapView.getMapAsync(this@MapFragment)
            mapView.onCreate(savedInstanceState)
        }
        MapLifeCycle(this, binding.mapView)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        viewDisposables += viewModel
            .getUserLocationOnce(requireActivity())
            ?.subscribe({
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            it.latitude,
                            it.longitude
                        ), zoomLevel
                    )
                )
            }, {
                it.printStackTrace()
            })
    }

}