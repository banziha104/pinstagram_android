package com.lyj.pinstagram.compose

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.lyj.pinstagram.extension.android.getDimen

interface ComposeLayoutContract<T> {
    val context : Context

    @Composable
    fun View(state: T)


    fun resString(@StringRes id: Int) = context.getString(id)

    fun resDimen(@DimenRes id: Int) = context.getDimen(id)

    fun resColor(@ColorRes id: Int) =  ContextCompat.getColor(context,id)
}