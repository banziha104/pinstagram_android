package com.lyj.pinstagram.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayout
import com.lyj.core.extension.lang.plusAssign
import com.lyj.core.rx.activity.AutoActivatedDisposable
import com.lyj.core.rx.activity.AutoClearedDisposable
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivityMainBinding
import com.lyj.pinstagram.extension.android.TabLayoutEventType
import com.lyj.pinstagram.extension.android.selectedObserver
import io.reactivex.rxjava3.disposables.Disposable

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory(application)
    }

    private val disposables = AutoClearedDisposable(this)
    private val viewDisposables = AutoActivatedDisposable(lifecycleOwner = this)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
        binding.viewModel = viewModel
        observeEvent()
    }

    private fun observeEvent() {
        viewDisposables += observeTabSelected()
    }

    private fun observeTabSelected(): Disposable =
        binding
            .mainTabLayout
            .selectedObserver(defaultPosition = 0)
            .filter { it == TabLayoutEventType.SELECTED }
            .subscribe({
                binding.invalidateAll()
                if (it.position != null) {
                    supportFragmentManager.commit {
                        replace(
                            R.id.mainFragmentContainer,
                            viewModel.tabItems[it.position!!].getFragment()
                        )
                    }
                }
            }, {
                it.printStackTrace()
            })
}