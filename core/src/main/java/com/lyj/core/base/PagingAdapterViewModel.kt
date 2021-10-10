package com.lyj.core.base

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil

interface PagingAdapterViewModel<T> {
    val context : Context
    val diffUtil : DiffUtil.ItemCallback<T>

    fun resString(
        @StringRes resId: Int
    ): String = context.getString(resId)


    fun resDimen(
        @DimenRes resId: Int
    ): Float = context.resources.getDimension(resId)


    fun resColor(
        @ColorRes resId: Int
    ): Int = ContextCompat.getColor(context,resId)

    fun resDrawble(
        @DrawableRes resId: Int
    ): Drawable = ContextCompat.getDrawable(context,resId)!!
}