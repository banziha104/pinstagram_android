package com.lyj.core.extension.android

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel

fun AndroidViewModel.resString(
    @StringRes resId: Int
): String = getApplication<Application>().applicationContext.getString(resId)


fun AndroidViewModel.resDimen(
    @DimenRes resId: Int
): Float = getApplication<Application>().applicationContext.resources.getDimension(resId)


fun AndroidViewModel.resColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(getApplication<Application>().applicationContext,resId)


fun AndroidViewModel.resDrawble(
    @DrawableRes resId: Int
): Drawable = ContextCompat.getDrawable(getApplication<Application>().applicationContext,resId)!!