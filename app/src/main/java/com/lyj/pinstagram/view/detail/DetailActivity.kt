package com.lyj.pinstagram.view.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.lyj.core.base.BaseActivity
import com.lyj.core.extension.testTag
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity :
    BaseActivity<DetailActivityViewModel, ActivityDetailBinding>(R.layout.activity_detail) {
    override val viewModel: DetailActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
        binding.viewModel = viewModel
        binding.detailProgressLayout.visibility = View.VISIBLE
        bindData(intent.extras?.get("id")?.toString()?.toLongOrNull())
    }

    private fun bindData(id : Long?){
        viewModel.requestContents(id ?: return makeWarningToast()){ response ->
            lifecycleScope.launch(Dispatchers.Main){
                if (!response.isOk || response.data == null){
                    makeWarningToast()
                    return@launch
                }

                binding.detailProgressLayout.visibility = View.GONE

                val data = response.data!!

                Glide
                    .with(this@DetailActivity)
                    .load(data.picture)
                    .into(binding.detailImage)
            }
        }
    }

    private fun makeWarningToast(){
        Toast.makeText(this, R.string.detail_contents_not_found_warning, Toast.LENGTH_LONG).show()
        binding.detailProgressLayout.visibility = View.GONE
    }
}