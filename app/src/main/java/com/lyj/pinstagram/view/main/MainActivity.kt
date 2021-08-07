package com.lyj.pinstagram.view.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.commit
import com.google.android.material.appbar.AppBarLayout
import com.iyeongjoon.nicname.core.rx.DisposableFunction
import com.lyj.core.base.BaseActivity
import com.lyj.core.extension.lang.plusAssign
import com.lyj.core.extension.testTag
import com.lyj.core.permission.PermissionManager
import com.lyj.domain.network.contents.ContentsTagType
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivityMainBinding
import com.lyj.pinstagram.extension.android.TabLayoutEventType
import com.lyj.pinstagram.extension.android.selectedObserver
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BaseActivity<MainActivityViewModel, ActivityMainBinding>(R.layout.activity_main) {
    @Inject
    internal lateinit var permissionManager: PermissionManager

    override val viewModel: MainActivityViewModel by viewModels()

    private var tabType: MainTabType = MainTabType.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
        binding.viewModel = viewModel
        observeEvent()
        observeLiveData()
    }

    private fun observeEvent() {
        viewDisposables += observeTopTabSelected()
        viewDisposables += observeBottomTabSelected()
        viewDisposables += observeOnceUserLocation()
    }

    private fun observeLiveData() {
        binding.mainTopTabs.let { tabLayout ->
            viewModel.originContentsList.observe(this) { list ->
                val group = list.groupBy { it.tag }
                if (group.isEmpty()){
                    tabLayout.visibility = View.GONE
                }else{
                    tabLayout.removeAllTabs()
                    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.main_top_tab_all)))
                    group.keys.forEach {
                        tabLayout.addTab(tabLayout.newTab().setText(getString(it.kor)))
                    }
                    tabLayout.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun observeTopTabSelected() : DisposableFunction = {
        binding
            .mainTopTabs
            .selectedObserver()
            .filter { it == TabLayoutEventType.SELECTED }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                if (it.text != null){
                    if (it.text == getString(R.string.main_top_tab_all)) {
                        viewModel.currentContentsList.postValue(viewModel.originContentsList.value)
                    }else{
                        val type = ContentsTagType.findByKoreanTitle(this, it.text!!)
                        if (type != null) viewModel.currentContentsList.postValue(viewModel.originContentsList.value?.filter { it.tag == type })
                    }
                    binding.invalidateAll()
                }
            }, {
                it.printStackTrace()
            })
    }

    private fun observeBottomTabSelected(): DisposableFunction = {
        binding
            .mainTabLayout
            .selectedObserver(defaultPosition = tabType.ordinal)
            .filter { it == TabLayoutEventType.SELECTED }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.position != null) {
                    supportFragmentManager.commit {
                        val type = viewModel.tabItems[it.position!!]
                        tabType = type
                        replace(
                            R.id.mainFragmentContainer,
                            type.getFragment()
                        )

                        binding.mainTopTabs.visibility =
                            if (type == MainTabType.TALK) View.GONE else View.VISIBLE
                    }
                }
                binding.invalidateAll()
            }, {
                it.printStackTrace()
            })
    }


    private fun observeOnceUserLocation(): DisposableFunction = {
        viewModel
            .getUserLocation(this)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(Schedulers.io())
            ?.flatMap {
                viewModel.location.postValue(viewModel.transLocationToAddress(it))
                viewModel.requestContentsData(it.latitude, it.longitude)
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                if (it.isOk && it.data != null) {
                    viewModel.originContentsList.postValue(it.data)
                    viewModel.currentContentsList.postValue(it.data)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.main_network_warning),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }, {
                Toast.makeText(this, getString(R.string.main_location_warning), Toast.LENGTH_LONG)
                    .show()
                it.printStackTrace()
            })
    }
}

class MainAppBarLayoutScrollController : AppBarLayoutScrollController {
    override fun stopScroll(
        appBarLayout: AppBarLayout,
        coordinatorLayout: CoordinatorLayout,
        toolbar: Toolbar
    ) {

        val appBarParams = toolbar.layoutParams as AppBarLayout.LayoutParams
        appBarParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
        toolbar.layoutParams = appBarParams

        val coordinatorLayoutParams = coordinatorLayout.layoutParams as CoordinatorLayout.LayoutParams
        coordinatorLayoutParams.behavior = null
        coordinatorLayout.layoutParams = coordinatorLayoutParams
    }

    override fun startScroll(
        appBarLayout: AppBarLayout,
        coordinatorLayout: CoordinatorLayout,
        toolbar: Toolbar
    ) {
        val appBarParams = toolbar.layoutParams as AppBarLayout.LayoutParams
        appBarParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        toolbar.layoutParams = appBarParams

        val coordinatorLayoutParams = coordinatorLayout.layoutParams as CoordinatorLayout.LayoutParams
        coordinatorLayoutParams.behavior = AppBarLayout.Behavior()
        coordinatorLayout.layoutParams = coordinatorLayoutParams
    }

}

interface AppBarLayoutScrollController {
    fun stopScroll(
        appBarLayout: AppBarLayout,
        coordinatorLayout: CoordinatorLayout,
        toolbar: Toolbar
    )

    fun startScroll(
        appBarLayout: AppBarLayout,
        coordinatorLayout: CoordinatorLayout,
        toolbar: Toolbar
    )
}