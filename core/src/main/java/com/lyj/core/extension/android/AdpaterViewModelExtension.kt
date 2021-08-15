package com.lyj.core.extension.android

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.base.AdapterViewModel
import com.lyj.core.base.BaseFragment

fun <T> AdapterViewModel<T>.resString(
    @StringRes resId: Int
): String = context.getString(resId)


fun <T> AdapterViewModel<T>.resDimen(
    @DimenRes resId: Int
): Float = context.resources.getDimension(resId)


fun <T> AdapterViewModel<T>.resColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(requireContext(),resId)

fun <T> AdapterViewModel<T>.resDrawable(
    @DrawableRes resId: Int
): Drawable = ContextCompat.getDrawable(context,resId)!!