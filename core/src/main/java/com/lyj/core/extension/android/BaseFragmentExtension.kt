package com.lyj.core.extension.android

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.base.BaseDialog
import com.lyj.core.base.BaseFragment
import com.lyj.core.base.getDimen


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL,VIEW_BINDING>.getString(
    @StringRes resId: Int
): String = requireContext().getString(resId)


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL,VIEW_BINDING>.getDimen(
    @DimenRes resId: Int
): Float = requireContext().getDimen(resId)


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL,VIEW_BINDING>.getColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(requireContext(),resId)