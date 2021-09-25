package com.lyj.core.extension.android.base

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lyj.core.base.BaseAdapter

fun <DATA_SOURCE, VIEW_HOLDER : RecyclerView.ViewHolder> BaseAdapter<DATA_SOURCE, VIEW_HOLDER>.resString(
    @StringRes resId: Int
): String = viewModel.context.getString(resId)


fun <DATA_SOURCE, VIEW_HOLDER : RecyclerView.ViewHolder> BaseAdapter<DATA_SOURCE,VIEW_HOLDER>.resDimen(
    @DimenRes resId: Int
): Float = viewModel.context.resources.getDimension(resId)


fun <DATA_SOURCE, VIEW_HOLDER : RecyclerView.ViewHolder> BaseAdapter<DATA_SOURCE,VIEW_HOLDER>.resColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(viewModel.context,resId)

fun <DATA_SOURCE, VIEW_HOLDER : RecyclerView.ViewHolder> BaseAdapter<DATA_SOURCE,VIEW_HOLDER>.resDrawable(
    @DrawableRes resId: Int
): Drawable = ContextCompat.getDrawable(viewModel.context,resId)!!