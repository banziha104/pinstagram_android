package com.lyj.pinstagram.extension.android

import android.util.Log
import com.google.android.material.tabs.TabLayout
import com.lyj.core.extension.permissionTag
import io.reactivex.rxjava3.core.Observable

fun TabLayout.selectedObserver(defaultPosition : Int? = null) : Observable<TabLayoutEventType> = Observable.create { emitter ->
    //Default Select
    if (defaultPosition != null) emitter.onNext(TabLayoutEventType.SELECTED.apply { position = defaultPosition })

    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
        override fun onTabSelected(tab: TabLayout.Tab?) {
            emitter.onNext(TabLayoutEventType.SELECTED.apply {
                position = tab?.position
                text = tab?.text?.toString()
            })
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            emitter.onNext(TabLayoutEventType.UNSELECTED.apply {
                position = tab?.position
                text = tab?.text?.toString()
            })
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
            emitter.onNext(TabLayoutEventType.RESELECTED.apply {
                position = tab?.position
                text = tab?.text?.toString()
            })
        }
    })
}

enum class TabLayoutEventType(
    var position : Int? = null,
    var text : String? = null
){
    SELECTED, UNSELECTED, RESELECTED
}
