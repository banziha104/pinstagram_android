package com.lyj.core.extension.android

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.base.BaseFragment


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL,VIEW_BINDING>.resString(
    @StringRes resId: Int
): String = requireContext().getString(resId)


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL,VIEW_BINDING>.resDimen(
    @DimenRes resId: Int
): Float = requireContext().resources.getDimension(resId)


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL,VIEW_BINDING>.resColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(requireContext(),resId)

fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL,VIEW_BINDING>.resDrawable(
    @DrawableRes resId: Int
): Drawable = ContextCompat.getDrawable(requireContext(),resId)!!