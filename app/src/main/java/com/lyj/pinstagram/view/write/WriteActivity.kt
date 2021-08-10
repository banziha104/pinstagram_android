package com.lyj.pinstagram.view.write

import android.os.Bundle
import androidx.activity.viewModels
import com.lyj.core.base.BaseActivity
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivityWriteBinding

class WriteActivity :
    BaseActivity<WriteActivityViewModel, ActivityWriteBinding>(R.layout.activity_write,
        { ActivityWriteBinding.inflate(it) }
    ) {
    override val viewModel: WriteActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}