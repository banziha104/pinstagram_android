package com.lyj.pinstagram.view

import android.view.View

interface ProgressController {
    val progressLayout : View

    val controlledView : Collection<View>

    fun showProgressLayout(){
        progressLayout.visibility = View.VISIBLE
        controlledView.forEach { it.isEnabled = false }
    }

    fun hideProgressLayout(){
        progressLayout.visibility = View.INVISIBLE
        controlledView.forEach { it.isEnabled = true }
    }
}