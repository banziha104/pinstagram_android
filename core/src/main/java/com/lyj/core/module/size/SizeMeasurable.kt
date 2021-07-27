package com.lyj.core.module.size

import android.util.DisplayMetrics
import com.lyj.core.module.NeedContext

interface SizeMeasurable : NeedContext {

    val metrics: DisplayMetrics
        get() = context.resources.displayMetrics

    val dpHeight : Float
        get() = metrics.heightPixels / metrics.density

    val dpWidth : Float
        get() = metrics.widthPixels / metrics.density

    val pxWidth : Int
        get() = metrics.widthPixels

    val pxHeight : Int
        get() = metrics.heightPixels
}