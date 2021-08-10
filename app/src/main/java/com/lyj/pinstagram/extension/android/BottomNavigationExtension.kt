package com.lyj.pinstagram.extension.android

import android.content.Context
import android.view.MenuItem
import androidx.core.view.forEach
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lyj.pinstagram.view.main.MainTabType
import io.reactivex.rxjava3.core.Observable

fun BottomNavigationView.selectedObserver(context: Context, default : MainTabType? = null) : Observable<MenuItem> = Observable.create { emitter ->
    this.setOnItemSelectedListener {
        emitter.onNext(it)
        return@setOnItemSelectedListener true
    }

    if (default != null){
        menu.forEach {
            if (it.title == context.getString(default.titleId)){
                emitter.onNext(it)
                return@forEach
            }
        }
    }
}