package com.lyj.pinstagram.view.main.fragments.event

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lyj.core.base.BaseFragment
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.EventFramgmentBinding
import com.lyj.pinstagram.view.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class EventFragment :
    BaseFragment<EventFragmentViewModel, EventFramgmentBinding>(R.layout.event_framgment,
        { inflater, view -> EventFramgmentBinding.inflate(inflater) }) {

    override val viewModel: EventFragmentViewModel by viewModels()

    private val activityViewModel: MainActivityViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel.currentLocation.observe(viewLifecycleOwner) {
            binding.root.setContent {
                root(
                    binding = binding,
                    stateFlow = viewModel.getEventState(it.latitude, it.longitude)
                )
            }
        }
    }
}