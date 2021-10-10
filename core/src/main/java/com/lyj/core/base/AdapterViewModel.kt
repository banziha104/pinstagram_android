package com.lyj.core.base

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

interface AdapterViewModel<T> {
    val items : Collection<T>
    val context : Context

    val itemCount : Int
        get() = items.size
}
