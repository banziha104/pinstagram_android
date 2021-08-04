package com.lyj.pinstagram.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.lyj.core.base.BaseActivity
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivityDetailBinding

class DetailActivity() : BaseActivity<DetailActivityViewModel,ActivityDetailBinding>(R.layout.activity_detail) {
    override val viewModel: DetailActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}