package com.lyj.core.base

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

interface AdapterViewModel<T> {
    val items : Collection<T>
    val context : Context

    val itemCount : Int
    get() = items.size

    fun resString(
        @StringRes resId: Int
    ): String = context.getString(resId)


    fun resDimen(
        @DimenRes resId: Int
    ): Float = context.getDimen(resId)


    fun resColor(
        @ColorRes resId: Int
    ): Int = ContextCompat.getColor(context,resId)

    fun resDrawble(
        @DrawableRes resId: Int
    ): Drawable = ContextCompat.getDrawable(context,resId)!!
}