package com.lyj.pinstagram.view.main.fragments.map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lyj.domain.model.network.contents.ContentsTagType
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.MapFragmentBinding
import com.lyj.pinstagram.databinding.TalkFragmentBinding
import com.lyj.pinstagram.lifecycle.MapLifeCycle
import com.lyj.pinstagram.view.detail.DetailActivity
import com.lyj.pinstagram.view.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MapFragment : Fragment(),
    OnMapReadyCallback {

    private val viewModel: MapFragmentViewModel by viewModels()
    private val mainViewModel: MainActivityViewModel by activityViewModels()
    private val zoomLevel = 16.5f

    private lateinit var map: GoogleMap
    private lateinit var binding: MapFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapMapView.getMapAsync(this)
        MapLifeCycle(lifecycle, binding.mapMapView)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        mainViewModel.currentLocation.observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.Main) {
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it, zoomLevel
                    )
                )
            }
        }

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
                if (item != null) {
                    startActivity(Intent(requireActivity(), DetailActivity::class.java).apply {
                        putExtra("id", item.contentsId)
                    })
                }
            }
        }
    }

}