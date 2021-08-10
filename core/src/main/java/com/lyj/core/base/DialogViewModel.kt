package com.lyj.core.base

import android.content.Context

interface DialogViewModel {
    val context : Context

    fun getString(id : Int) : String = context.getString(id)
    fun getDimen(id : Int) = context.getDimen(id)
}

fun Context.getDimen(id : Int) = resources.getDimension(id)