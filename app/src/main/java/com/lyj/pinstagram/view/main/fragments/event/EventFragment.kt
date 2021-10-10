package com.lyj.pinstagram.view.main.fragments.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.EventFramgmentBinding
import com.lyj.pinstagram.view.main.MainActivityViewModel
import com.lyj.pinstagram.view.main.fragments.event.layouts.View
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment : Fragment() {

    private val viewModel: EventFragmentViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    lateinit var binding: EventFramgmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EventFramgmentBinding.inflate(inflater,container,false)
        return binding.root
    }

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