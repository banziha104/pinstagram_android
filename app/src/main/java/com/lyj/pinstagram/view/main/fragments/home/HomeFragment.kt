package com.lyj.pinstagram.view.main.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.lyj.core.base.BaseFragment
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.HomeFragmentBinding
import com.lyj.pinstagram.view.detail.DetailActivity
import com.lyj.pinstagram.view.main.MainActivityViewModel
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridAdapter
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridItem
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridViewModel

class HomeFragment() : BaseFragment<HomeFragmentViewModel, HomeFragmentBinding>(
    R.layout.home_fragment,
    { layoutInflater, viewGroup ->
        HomeFragmentBinding.inflate(
            layoutInflater,
            viewGroup,
            false
        )
    }
) {

    companion object {
        const val NUMBER_OF_COLUMNS = 3
    }

    override val viewModel: HomeFragmentViewModel by viewModels()
    private val mainViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun observeLiveData() {
        mainViewModel.currentContentsList.observe(viewLifecycleOwner) { response ->
            binding.homeRecyclerView.let { gridView ->
                gridView.adapter = HomeGridAdapter(
                    HomeGridViewModel(
                        response.map { HomeGridItem.fromResponse(it) },
                        requireContext(),
                    ) {
                        startActivity(Intent(requireActivity(), DetailActivity::class.java).apply {
                            putExtra("id", it.id)
                        })
                    }
                )
                gridView.layoutManager = GridLayoutManager(requireContext(), NUMBER_OF_COLUMNS)
                gridView.adapter?.notifyDataSetChanged()
            }
        }
    }

}