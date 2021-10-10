package com.lyj.pinstagram.view.main.fragments.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.lyj.core.extension.base.resString
import com.lyj.core.extension.lang.testTag
import com.lyj.core.rx.DisposableLifecycleController
import com.lyj.core.rx.RxLifecycleObserver
import com.lyj.core.rx.disposeByOnDestory
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.HomeFragmentBinding
import com.lyj.pinstagram.view.detail.DetailActivity
import com.lyj.pinstagram.view.main.MainActivityViewModel
import com.lyj.pinstagram.view.main.RequestChangeCurrentLocation
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridAdapter
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridItem
import com.lyj.pinstagram.view.main.fragments.home.adapter.HomeGridViewModel
import java.util.concurrent.TimeUnit

class HomeFragment :Fragment(),DisposableLifecycleController {


    companion object {
        const val NUMBER_OF_COLUMNS = 3
        private val DEFAULT_LAT_LNG = 37.385940 to 127.122450
    }

    private val viewModel: HomeFragmentViewModel by viewModels()
    private val mainViewModel: MainActivityViewModel by activityViewModels()
    override val disposableLifecycleObserver: RxLifecycleObserver = RxLifecycleObserver(this)
    private lateinit var binding: HomeFragmentBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        bindButton()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
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
                            this
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

    private fun bindButton(){
        binding
            .homeTxtEmpty
            .clicks()
            .disposeByOnDestory(this)
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
