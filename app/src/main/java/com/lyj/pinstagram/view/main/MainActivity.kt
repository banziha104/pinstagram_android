package com.lyj.pinstagram.view.main

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

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
        observeLiveData()
    }

    private fun observeEvent() {
        viewDisposables += observeTabSelected()
        viewDisposables += observeOnceUserLocation()
    }

    private fun observeLiveData(){

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
        ?.subscribeOn(Schedulers.io())
        ?.observeOn(Schedulers.io())
        ?.flatMap {
            viewModel.location.postValue( viewModel.transLocationToAddress(it))
            viewModel.requestContentsData(it.latitude,it.longitude)
        }
        ?.observeOn(AndroidSchedulers.mainThread())
        ?.subscribe({
            if (it.isOk && it.data != null){
                viewModel.contentsList.postValue(it.data)
            }else{
                Toast.makeText(this, getString(R.string.main_network_warning), Toast.LENGTH_LONG)
                    .show()
            }
        }, {
            Toast.makeText(this, getString(R.string.main_location_warning), Toast.LENGTH_LONG)
                .show()
            it.printStackTrace()
        })
}