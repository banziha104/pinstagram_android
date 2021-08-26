package com.lyj.pinstagram.view.main.fragments.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lyj.core.base.BaseFragment
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.EventFramgmentBinding

class EventFragment() :
    BaseFragment<EventFragmentViewModel, EventFramgmentBinding>(R.layout.event_framgment,
        { inflater, view -> EventFramgmentBinding.inflate(inflater) }) {

    override val viewModel: EventFragmentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setContent {  }
    }
}