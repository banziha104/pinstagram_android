package com.lyj.pinstagram.view.main.fragments.map

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lyj.core.base.BaseFragment
import com.lyj.core.extension.lang.plusAssign
import com.lyj.domain.network.contents.ContentsTagType
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.MapFragmentBinding
import com.lyj.pinstagram.lifecycle.MapLifeCycle
import com.lyj.pinstagram.view.detail.DetailActivity
import com.lyj.pinstagram.view.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint




@AndroidEntryPoint
class MapFragment() : BaseFragment<MapFragmentViewModel, MapFragmentBinding>(
    R.layout.map_fragment,
    { layoutInflater, viewGroup ->
        MapFragmentBinding.inflate(
            layoutInflater,
            viewGroup,
            false
        )
    }
),
    OnMapReadyCallback {

    override val viewModel: MapFragmentViewModel by viewModels()
    private val mainViewModel: MainActivityViewModel by activityViewModels()

    private val zoomLevel = 16.5f

    private lateinit var map: GoogleMap


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapMapView.getMapAsync(this)
        MapLifeCycle(lifecycle, binding.mapMapView)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        disposables += viewModel
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



        mainViewModel.currentContentsList.observe(this) { response ->
            map.clear()
            response.forEach {
                map.addMarker(
                    MarkerOptions()
                        .position(LatLng(it.lat, it.lng))
                        .title(it.title)
                        .icon(
                            BitmapDescriptorFactory.fromResource(
                                when (it.tag) {
                                    ContentsTagType.FOOD -> R.drawable.pin_food
                                    ContentsTagType.SHOP -> R.drawable.pin_shop
                                    ContentsTagType.PLACE -> R.drawable.pin_place
                                    ContentsTagType.SERVICE -> R.drawable.pin_service
                                    else -> R.drawable.pin_etc
                                }
                            )
                        )
                )
            }

            map.setOnInfoWindowClickListener { marker ->
                val item = response.firstOrNull { it.title == marker.title }
                if (item != null){
                    startActivity(Intent(requireActivity(), DetailActivity::class.java).apply {
                        putExtra("id", item.contentsId)
                    })
                }
            }
        }
    }

}