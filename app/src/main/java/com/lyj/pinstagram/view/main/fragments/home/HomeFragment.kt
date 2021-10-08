package com.lyj.pinstagram.view.main.fragments.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.lyj.core.base.BaseFragment
import com.lyj.core.extension.android.fromStartToStopScope
import com.lyj.core.extension.android.resString
import com.lyj.core.extension.lang.plusAssign
import com.lyj.core.extension.lang.testTag
import com.lyj.core.rx.DisposableFunction
import com.lyj.core.rx.plusAssign
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.HomeFragmentBinding
import com.lyj.pinstagram.view.detail.DetailActivity
import com.lyj.pinstagram.view.main.MainActivityViewModel
import com.lyj.pinstagram.view.main.RequestChangeCurrentLocation
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridAdapter
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridItem
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridViewModel
import java.util.concurrent.TimeUnit

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
        private val DEFAULT_LAT_LNG = 37.385940 to 127.122450
    }

    override val viewModel: HomeFragmentViewModel by viewModels()
    private val mainViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        fromStartToStopScope += bindButton()
    }

    private fun observeLiveData() {
        mainViewModel.currentContentsList.observe(viewLifecycleOwner) { response ->
            if (response.isNotEmpty()) {
                binding.homeRecyclerView.let { gridView ->
                    binding.homeRecyclerView.visibility = View.VISIBLE
                    binding.homeTxtEmpty.visibility = View.GONE
                    gridView.adapter = HomeGridAdapter(
                        HomeGridViewModel(
                            response.map { HomeGridItem.fromResponse(it) },
                            requireContext(),
                            scopes
                        ) {
                            startActivity(
                                Intent(
                                    requireActivity(),
                                    DetailActivity::class.java
                                ).apply {
                                    putExtra("id", it.id)
                                })
                        }
                    )
                    gridView.layoutManager = GridLayoutManager(requireContext(), NUMBER_OF_COLUMNS)
                    gridView.adapter?.notifyDataSetChanged()
                }
            } else {
                binding.homeRecyclerView.visibility = View.GONE
                binding.homeTxtEmpty.visibility = View.VISIBLE
                binding.homeTxtEmpty.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(
                        resString(R.string.home_fragment_empty),
                        Html.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    Html.fromHtml(resString(R.string.home_fragment_empty))
                }
            }
        }
    }

    private fun bindButton(): DisposableFunction = {
        binding
            .homeTxtEmpty
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                Log.d(testTag,"dd ${ (activity as? RequestChangeCurrentLocation)}")
                (activity as? RequestChangeCurrentLocation)?.requestCurrentLocation(
                    DEFAULT_LAT_LNG.first,
                    DEFAULT_LAT_LNG.second
                )
            }
    }
}
