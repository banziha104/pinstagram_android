package com.lyj.core.extension.android

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.base.BaseActivity
import com.lyj.core.base.BaseDialog
import com.lyj.core.base.getDimen


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseDialog<VIEW_BINDING,VIEW_MODEL>.getString(
    @StringRes resId: Int
): String = requireContext().getString(resId)


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseDialog<VIEW_BINDING,VIEW_MODEL>.getDimen(
    @DimenRes resId: Int
): Float = requireContext().getDimen(resId)


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseDialog<VIEW_BINDING,VIEW_MODEL>.getColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(requireContext(),resId)