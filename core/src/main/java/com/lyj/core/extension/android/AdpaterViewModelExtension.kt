package com.lyj.core.extension.android

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.lyj.core.base.AdapterViewModel

fun <T> AdapterViewModel<T>.resString(
    @StringRes resId: Int
): String = context.getString(resId)


fun <T> AdapterViewModel<T>.resDimen(
    @DimenRes resId: Int
): Float = context.resources.getDimension(resId)


fun <T> AdapterViewModel<T>.resColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(context,resId)

fun <T> AdapterViewModel<T>.resDrawable(
    @DrawableRes resId: Int
): Drawable = ContextCompat.getDrawable(context,resId)!!