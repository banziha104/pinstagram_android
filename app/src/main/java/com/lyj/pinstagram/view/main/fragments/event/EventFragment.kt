package com.lyj.pinstagram.view.main.fragments.event

import android.os.Bundle
import android.view.View
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lyj.core.base.BaseFragment
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.EventFramgmentBinding
import com.lyj.pinstagram.view.main.MainActivityViewModel
import com.lyj.pinstagram.view.main.fragments.event.layouts.View
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment :
    BaseFragment<EventFragmentViewModel, EventFramgmentBinding>(R.layout.event_framgment,
        { inflater, view -> EventFramgmentBinding.inflate(inflater) }) {

    override val viewModel: EventFragmentViewModel by viewModels()

    private val activityViewModel: MainActivityViewModel by activityViewModels()


    @ExperimentalAnimationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel.currentLocation.observe(viewLifecycleOwner) {
            binding.root.setContent {
                View(
                    requireContext(),
                    stateFlow = viewModel.getEventState(it.latitude, it.longitude)
                )
            }
        }
    }
}