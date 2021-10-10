package com.lyj.core.extension.android

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

fun Activity.resString(
    @StringRes resId: Int
): String = applicationContext.getString(resId)


fun Activity.resDimen(
    @DimenRes resId: Int
): Float = applicationContext.resources.getDimension(resId)


fun Activity.resColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(applicationContext,resId)

fun Activity.resDrawble(
    @DrawableRes resId: Int
): Drawable = ContextCompat.getDrawable(applicationContext,resId)!!


