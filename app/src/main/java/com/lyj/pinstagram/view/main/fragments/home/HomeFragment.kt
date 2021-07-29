package com.lyj.pinstagram.view.main.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lyj.core.base.BaseFragment
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.HomeFragmentBinding
import com.lyj.pinstagram.view.main.fragments.map.MapFragment

class HomeFragment  private constructor(): BaseFragment<HomeFragmentViewModel,HomeFragmentBinding>(R.layout.home_fragment) {

    companion object{
        val instance : HomeFragment by lazy { HomeFragment() }
    }

    override val viewModel: HomeFragmentViewModel by viewModels()
}