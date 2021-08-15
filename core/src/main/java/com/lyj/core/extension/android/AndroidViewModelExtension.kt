package com.lyj.core.extension.android

import android.app.Application
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.base.BaseFragment
import com.lyj.core.base.getDimen


fun AndroidViewModel.getString(
    @StringRes resId: Int
): String = getApplication<Application>().applicationContext.getString(resId)


fun AndroidViewModel.getDimen(
    @DimenRes resId: Int
): Float = getApplication<Application>().applicationContext.getDimen(resId)


fun AndroidViewModel.getColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(getApplication<Application>().applicationContext,resId)