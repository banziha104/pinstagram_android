package com.lyj.core.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<VIEW_BINDING : ViewBinding>(
    viewModel: DialogViewModel,
    private val factory: (LayoutInflater,ViewGroup?,Bundle?) -> VIEW_BINDING
) : DialogFragment() {
    protected lateinit var binding: VIEW_BINDING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = factory(layoutInflater,container,savedInstanceState)
        return binding.root
    }
}