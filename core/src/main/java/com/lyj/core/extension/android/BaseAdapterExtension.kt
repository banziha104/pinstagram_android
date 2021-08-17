package com.lyj.core.extension.android

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.lyj.core.base.BaseAdapter

fun <T> BaseAdapter<T>.resString(
    @StringRes resId: Int
): String = viewModel.context.getString(resId)


fun <T> BaseAdapter<T>.resDimen(
    @DimenRes resId: Int
): Float = viewModel.context.resources.getDimension(resId)


fun <T> BaseAdapter<T>.resColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(viewModel.context,resId)

fun <T> BaseAdapter<T>.resDrawable(
    @DrawableRes resId: Int
): Drawable = ContextCompat.getDrawable(viewModel.context,resId)!!