package com.lyj.core.extension.android

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.base.BaseActivity
import com.lyj.core.base.BaseDialog
import com.lyj.core.base.BaseFragment


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseDialog<VIEW_BINDING,VIEW_MODEL>.resString(
    @StringRes resId: Int
): String = requireContext().getString(resId)


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseDialog<VIEW_BINDING,VIEW_MODEL>.resDimen(
    @DimenRes resId: Int
): Float = requireContext().resources.getDimension(resId)


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseDialog<VIEW_BINDING,VIEW_MODEL>.getColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(requireContext(),resId)

fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseDialog<VIEW_BINDING, VIEW_MODEL>.resDrwable(
    @DrawableRes resId: Int
): Drawable = ContextCompat.getDrawable(requireContext(),resId)!!