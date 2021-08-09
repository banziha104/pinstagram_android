package com.lyj.customui.dialog.map

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.gms.maps.MapView
import com.lyj.customui.R

class UserInteractionControllableMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MapView(context, attrs, defStyleAttr) {
    private val canTouch : Boolean
    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.UserInteractionControllableMapView,
            defStyleAttr,
            0
        ).let { attr ->

            canTouch = attr.getBoolean(R.styleable.UserInteractionControllableMapView_canUserInteraction, false)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(canTouch)
        return super.dispatchTouchEvent(ev)
    }
}