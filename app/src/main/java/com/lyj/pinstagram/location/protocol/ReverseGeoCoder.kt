package com.lyj.pinstagram.location.protocol

import android.location.Address
import android.location.Location

interface ReverseGeoCoder {
    fun transLocationToAddress(location : Location) : Address
}