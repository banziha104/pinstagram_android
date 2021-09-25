package com.lyj.core.extension.base

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lyj.core.base.AdapterViewModel
import com.lyj.core.base.BaseAdapter

fun <DATA_SOURCE> AdapterViewModel<DATA_SOURCE>.resString(
    @StringRes resId: Int
): String = context.getString(resId)


fun <DATA_SOURCE> AdapterViewModel<DATA_SOURCE>.resDimen(
    @DimenRes resId: Int
): Float = context.resources.getDimension(resId)


fun <DATA_SOURCE> AdapterViewModel<DATA_SOURCE>.resColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(context, resId)


fun <DATA_SOURCE> AdapterViewModel<DATA_SOURCE>.resDrawble(
    @DrawableRes resId: Int
): Drawable = ContextCompat.getDrawable(context, resId)!!