package com.lyj.pinstagram.view.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lyj.core.base.BaseAdapter
import com.lyj.core.extension.android.resString
import com.lyj.pinstagram.R
import com.lyj.pinstagram.lifecycle.MapLifeCycle


class DetailAdapter(override val viewModel: DetailAdapterViewModel) :
    RecyclerView.Adapter<DetailViewHolder>(), BaseAdapter<DetailItemType> {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder =
        when (viewType) {
            DetailAdapterViewType.TEXT.type -> {
                DetailViewHolder.DetailTextViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_detail_text, parent, false)
                )
            }
            DetailAdapterViewType.MAP.type -> {
                DetailViewHolder.DetailMapViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_detail_map, parent, false)
                )
            }
            else -> {
                DetailViewHolder.DetailImageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_detail_image, parent, false)
                )
            }
        }

    override fun getItemCount(): Int = viewModel.itemCount


    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        viewModel.items[position].let { item ->
            when (holder) {
                is DetailViewHolder.DetailImageViewHolder -> {
                    Glide
                        .with(viewModel.context)
                        .load(item.parseData<DetailParsedType.StringParsedType>(viewModel.data).item)
                        .into(holder.imageView)
                }
                is DetailViewHolder.DetailMapViewHolder -> {
                    holder.title.text = resString(item.title!!)
                    holder.setUpLatLng(item.parseData<DetailParsedType.LatLngParsedType>(viewModel.data).item)
                    holder.mapView.getMapAsync(holder)
                    MapLifeCycle(viewModel.lifecycle, holder.mapView)
                }
                is DetailViewHolder.DetailTextViewHolder -> {
                    holder.title.text = resString(item.title!!)
                    when(item){
                        DetailItemType.TAG -> {
                            holder.contents.text = resString(item.parseData<DetailParsedType.IntParsedType>(viewModel.data).item)
                        }
                        DetailItemType.TITLE, DetailItemType.DESCRIPTION ,DetailItemType.FULL_ADDRESS -> {
                            holder.contents.text = item.parseData<DetailParsedType.StringParsedType>(viewModel.data).item
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewModel.items[position].viewType.type
    }
}

sealed class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class DetailImageViewHolder(view: View) : DetailViewHolder(view) {
        val imageView: ImageView = view.findViewById<ImageView>(R.id.detailItemImage)
    }

    class DetailTextViewHolder(view: View) : DetailViewHolder(view) {
        val title: TextView = view.findViewById<TextView>(R.id.detailItemTextTitle)
        val contents: TextView = view.findViewById<TextView>(R.id.detailItemTextContent)
    }

    class DetailMapViewHolder(view: View) : DetailViewHolder(view), OnMapReadyCallback {
        val title: TextView = view.findViewById<TextView>(R.id.detailItemMapTitle)
        val mapView: MapView = view.findViewById<MapView>(R.id.detailItemMapView)

        private val zoomLevel = 16.5f
        private var latLng: LatLng? = null

        var map: GoogleMap? = null

        fun setUpLatLng(latLng: LatLng?) {
            this.latLng = latLng
        }

        override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap

            map?.uiSettings?.isScrollGesturesEnabled = false

            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng, zoomLevel
                )
            )

            map?.addMarker(
                MarkerOptions()
                    .position(latLng)
            )
        }
    }
}