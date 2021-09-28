package com.lyj.core.extension.android

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.lyj.core.base.BaseDialog
import com.lyj.core.base.BaseFragment
import com.lyj.core.rx.*


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL, VIEW_BINDING>.resString(
    @StringRes resId: Int
): String = requireContext().getString(resId)


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL, VIEW_BINDING>.resDimen(
    @DimenRes resId: Int
): Float = requireContext().resources.getDimension(resId)


fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL, VIEW_BINDING>.resColor(
    @ColorRes resId: Int
): Int = ContextCompat.getColor(requireContext(), resId)

fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL, VIEW_BINDING>.resDrawable(
    @DrawableRes resId: Int
): Drawable = ContextCompat.getDrawable(requireContext(), resId)!!


// RxJava LifeCycle
val <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL, VIEW_BINDING>.fromImmediatelyToDestroyScope
    get() = DisposableScope(EntryPoint.IMMEDIATELY at EndPoint.DESTROY, disposableController)

val <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL, VIEW_BINDING>.fromCreateToDestroyScope
    get() = DisposableScope(EntryPoint.CREATE at EndPoint.DESTROY, disposableController)

val <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL, VIEW_BINDING>.fromStartToStopScope
    get() = DisposableScope(EntryPoint.START at EndPoint.STOP, disposableController)

val <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL, VIEW_BINDING>.fromResumeToPause
    get() = DisposableScope(EntryPoint.RESUME at EndPoint.PAUSE, disposableController)

fun <VIEW_MODEL : ViewModel, VIEW_BINDING : ViewBinding> BaseFragment<VIEW_MODEL, VIEW_BINDING>.customScope(
    lifecycle: DisposableLifecycle,
) = DisposableScope(lifecycle, disposableController)
