package com.lyj.data.source.android.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.lyj.data.source.android.location.protocol.ReverseGeoCoder
import java.io.IOException
import java.util.*

class GeoCodeManager(
    val context : Context
) : ReverseGeoCoder {
    private val locale = Locale.KOREA
    private val maxResult = 1
    private val coder by lazy {  Geocoder(context,locale) }
    private val defaultAddress : Address = Address(Locale.KOREA).apply {
        latitude = 37.387056
        longitude = 127.123057
    }

    override fun transLocationToAddress(location: Location): Address = coder.getFromLocation(location.latitude,location.longitude,maxResult).let {
        try {
            if (it.isNotEmpty()) {
                return it.first()
            }
            return defaultAddress
        }catch (e : IOException){
            return defaultAddress
        }
    }
}