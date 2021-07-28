package com.lyj.pinstagram.view.main.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lyj.pinstagram.R
import com.lyj.pinstagram.view.main.fragments.map.MapFragment

class HomeFragment : Fragment() {

    companion object{
        val instance : HomeFragment by lazy { HomeFragment() }
    }

    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }
}