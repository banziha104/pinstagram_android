package com.lyj.pinstagram.view.main.fragments.map

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lyj.pinstagram.R
import com.lyj.pinstagram.view.main.fragments.talk.TalkFragment

class MapFragment : Fragment() {

    companion object{
        val instance : MapFragment by lazy { MapFragment() }
    }

    private val viewModel: MapFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

}