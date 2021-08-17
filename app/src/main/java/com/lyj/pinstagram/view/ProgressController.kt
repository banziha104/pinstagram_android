package com.lyj.pinstagram.view

import android.view.View

interface ProgressController {
    val progressLayout : View


    fun showProgressLayout(controlledViews : Collection<View>? = null){
        progressLayout.visibility = View.VISIBLE
        controlledViews?.forEach { it.isEnabled = false }
    }

    fun hideProgressLayout(controlledViews : Collection<View>? = null){
        progressLayout.visibility = View.INVISIBLE
        controlledViews?.forEach { it.isEnabled = true }
    }
}