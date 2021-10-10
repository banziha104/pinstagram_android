package com.lyj.core.extension.base

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lyj.core.rx.*


fun Fragment.resString(
    @StringRes resId: Int
): String = requireContext().getString(resId)

fun Fragment.resDimen(
    @DimenRes resId: Int
): Float = requireContext().resources.getDimension(resId)


fun Fragment.getColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(requireContext(), resId)

fun Fragment.resDrwable(
    @DrawableRes resId: Int
): Drawable = ContextCompat.getDrawable(requireContext(), resId)!!