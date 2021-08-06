package com.lyj.pinstagram.view.main.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.lyj.core.base.BaseFragment
import com.lyj.core.extension.testTag
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.HomeFragmentBinding
import com.lyj.pinstagram.view.main.MainActivityViewModel
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridAdapter
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridItem
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridViewModel
import com.lyj.pinstagram.view.main.fragments.map.MapFragment

class HomeFragment private constructor() :
    BaseFragment<HomeFragmentViewModel, HomeFragmentBinding>(R.layout.home_fragment) {

    companion object {
        val instance: HomeFragment by lazy { HomeFragment() }
        const val NUMBER_OF_COLUMNS = 3
    }

    override val viewModel: HomeFragmentViewModel by viewModels()
    private val mainViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
        binding.viewModel = viewModel
        observeLiveData()
    }

    private fun observeLiveData() {
        mainViewModel.contentsList.observe(viewLifecycleOwner) { response ->
            binding.homeGridView.let { gridView ->
                gridView.adapter = HomeGridAdapter(
                    HomeGridViewModel(
                        response.map { HomeGridItem.fromResponse(it) },
                        requireContext()
                    )
                )
                gridView.layoutManager = GridLayoutManager(requireContext(), NUMBER_OF_COLUMNS)
                gridView.adapter?.notifyDataSetChanged()
            }
        }
    }
}