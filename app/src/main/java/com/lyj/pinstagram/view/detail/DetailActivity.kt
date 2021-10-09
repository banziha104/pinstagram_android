package com.lyj.pinstagram.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyj.core.base.BaseActivity
import com.lyj.data.source.remote.entity.contents.response.ContentsRetrieveResponse
import com.lyj.domain.model.network.contents.ContentsModel
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivityDetailBinding
import com.lyj.pinstagram.view.ProgressController
import com.lyj.pinstagram.view.detail.adapter.DetailAdapter
import com.lyj.pinstagram.view.detail.adapter.DetailAdapterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity :
    BaseActivity<DetailActivityViewModel, ActivityDetailBinding>(R.layout.activity_detail,
        { ActivityDetailBinding.inflate(it) }) , ProgressController{

    companion object {
        const val DATA_NOT_RESOLVED = 201
    }

    override val progressLayout: View by lazy { binding.detailProgressLayout }

    override val viewModel: DetailActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProgressLayout()
        bindData(intent.extras?.get("id")?.toString()?.toLongOrNull())
    }

    private fun bindData(id: Long?) {
        viewModel.requestContents(id ?: return makeWarningToast()) { response ->
            lifecycleScope.launch(Dispatchers.Main) {

                hideProgressLayout()

                if (!response.isOk || response.data == null) {
                    makeWarningToast()
                    return@launch
                }

                setUpRecyclerView(response.data!!)
            }
        }
    }

    private fun setUpRecyclerView(data: ContentsModel) {
        binding.detailRecyclerView.apply {
            adapter = DetailAdapter(DetailAdapterViewModel(context, data, scopes, lifecycle))
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun makeWarningToast() {
        Toast.makeText(this, R.string.detail_contents_not_found_warning, Toast.LENGTH_LONG).show()
        finishActivity(DATA_NOT_RESOLVED)
    }
}