package com.lyj.pinstagram.location

import android.location.Address
import android.location.Location

interface ReverseGeoCoder {
    fun transLocationToAddress(location : Location) : Address?
}