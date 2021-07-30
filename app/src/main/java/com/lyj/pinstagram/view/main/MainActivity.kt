package com.lyj.pinstagram.view.main

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.lyj.core.base.BaseActivity
import com.lyj.core.extension.lang.plusAssign
import com.lyj.core.extension.mapTag
import com.lyj.core.extension.simpleTag
import com.lyj.core.permission.PermissionManager
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivityMainBinding
import com.lyj.pinstagram.extension.android.TabLayoutEventType
import com.lyj.pinstagram.extension.android.selectedObserver
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BaseActivity<MainActivityViewModel, ActivityMainBinding>(R.layout.activity_main) {
    @Inject
    internal lateinit var permissionManager: PermissionManager

    override val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
        binding.viewModel = viewModel
        observeEvent()
    }

    private fun observeEvent() {
        viewDisposables += observeTabSelected()
        viewDisposables += observeOnceUserLocation()
    }

    private fun observeTabSelected(): Disposable =
        binding
            .mainTabLayout
            .selectedObserver(defaultPosition = MainTabType.HOME.ordinal)
            .filter { it == TabLayoutEventType.SELECTED }
            .subscribe({
                if (it.position != null) {
                    supportFragmentManager.commit {
                        replace(
                            R.id.mainFragmentContainer,
                            viewModel.tabItems[it.position!!].getFragment()
                        )
                    }
                }
                binding.invalidateAll()
            }, {
                it.printStackTrace()
            })

    private fun observeOnceUserLocation() = viewModel
        .getUserLocation(this)
        ?.subscribe({
            Log.d(mapTag, "location $it")
            val result = viewModel.transLocationToAddress(it)
            Log.d(mapTag, "address $result")

        }, {
            it.printStackTrace()
        })
}