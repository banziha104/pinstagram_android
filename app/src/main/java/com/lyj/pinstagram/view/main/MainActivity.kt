package com.lyj.pinstagram.view.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.iyeongjoon.nicname.core.rx.DisposableFunction
import com.jakewharton.rxbinding4.view.clicks
import com.lyj.core.base.BaseActivity
import com.lyj.core.extension.lang.plusAssign
import com.lyj.core.extension.lang.testTag
import com.lyj.core.permission.PermissionManager
import com.lyj.domain.network.contents.ContentsTagType
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivityMainBinding
import com.lyj.pinstagram.extension.android.TabLayoutEventType
import com.lyj.pinstagram.extension.android.selectedObserver
import com.lyj.pinstagram.view.main.dialogs.sign.SignDialog
import com.lyj.pinstagram.view.main.dialogs.sign.SignDialogViewModel
import com.lyj.pinstagram.view.main.dialogs.write.WriteDialog
import com.lyj.pinstagram.view.main.dialogs.write.WriteDialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BaseActivity<MainActivityViewModel, ActivityMainBinding>(R.layout.activity_main,
        { ActivityMainBinding.inflate(it) }) {
    @Inject
    internal lateinit var permissionManager: PermissionManager

    override val viewModel: MainActivityViewModel by viewModels()

    private var tabType: MainTabType = MainTabType.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainProgressLayout.visibility = View.VISIBLE
        observeEvent()
        observeLiveData()
    }

    private fun observeEvent() {
        viewDisposables += observeTopTabSelected()
        viewDisposables += observeBottomTabSelected()
        viewDisposables += observeOnceUserLocation()
        viewDisposables += observeFloatingButton()
        viewDisposables += observeAuthButton()
        viewDisposables += observeToken()
    }

    private fun observeLiveData() {
        binding.mainTopTabs.let { tabLayout ->
            viewModel.originContentsList.observe(this) { list ->
                val group = list.groupBy { it.tag }
                if (group.isEmpty()) {
                    tabLayout.visibility = View.GONE
                } else {
                    tabLayout.removeAllTabs()
                    tabLayout.addTab(
                        tabLayout.newTab().setText(getString(R.string.main_top_tab_all))
                    )
                    group.keys.forEach {
                        tabLayout.addTab(tabLayout.newTab().setText(getString(it.kor)))
                    }
                    tabLayout.visibility = View.VISIBLE
                    binding.mainProgressLayout.visibility = View.GONE
                }
            }
        }
    }

    private fun observeToken(): DisposableFunction = {
        viewModel
            .getTokenObserve()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(testTag,"token : ${it.map { it.token }.joinToString(",")}")
                binding.btnMainAuth.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        if (it.isNotEmpty() && it.first().token.isNotBlank()) R.drawable.user_icon_login
                        else R.drawable.user_icon
                    )
                )
            }, {

            })
    }

    private fun observeAuthButton(): DisposableFunction = {
        binding
            .btnMainAuth
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .flatMapSingle {
                viewModel
                    .getUserToken()
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    SignDialog(SignDialogViewModel(this)).show(supportFragmentManager, null)
                } else {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.main_logout_dialog_title))
                        .setMessage(getString(R.string.main_logout_dialog_content))
                        .setPositiveButton(getString(R.string.main_logout_dialog_positive)) { dialog, _ ->
                            viewModel
                                .deleteToken()
                                .subscribe({
                                    dialog.dismiss()
                                }, {
                                    Toast.makeText(
                                        this,
                                        R.string.main_logout_warning,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    dialog.dismiss()
                                    it.printStackTrace()
                                })
                        }
                        .setNegativeButton(
                            getString(R.string.main_logout_dialog_negative)
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }, {
                Toast.makeText(this, R.string.main_logout_warning, Toast.LENGTH_LONG).show()
                it.printStackTrace()
            })
    }

    private fun observeFloatingButton(): DisposableFunction = {

        binding
            .mainFloatingButton
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                WriteDialog(
                    WriteDialogViewModel(
                        this,
                        viewModel.contentsService,
                        viewDisposables,
                        viewModel.locationEventManager,
                        viewModel.storageUploader
                    )
                ).show(supportFragmentManager, null)
            }, {
                it.printStackTrace()
            })
    }

    private fun observeTopTabSelected(): DisposableFunction = {
        binding
            .mainTopTabs
            .selectedObserver()
            .filter { it == TabLayoutEventType.SELECTED }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.text != null) {
                    if (it.text == getString(R.string.main_top_tab_all)) {
                        viewModel.currentContentsList.postValue(viewModel.originContentsList.value)
                    } else {
                        val type = ContentsTagType.findByKoreanTitle(this, it.text!!)
                        if (type != null) viewModel.currentContentsList.postValue(viewModel.originContentsList.value?.filter { it.tag == type })
                    }
                }
            }, {
                it.printStackTrace()
            })
    }

    private fun observeBottomTabSelected(): DisposableFunction = {
        binding
            .mainBottomNavigation
            .selectedObserver(this, default = MainTabType.HOME)
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                when (it.title) {
                    getString(R.string.main_tap_home_title) -> MainTabType.HOME
                    getString(R.string.main_tap_map_title) -> MainTabType.MAP
                    getString(R.string.main_tap_talk_title) -> MainTabType.TALK
                    else -> MainTabType.HOME
                }
            }
            .subscribe({ type ->
                supportFragmentManager.commit {
                    tabType = type
                    replace(
                        R.id.mainFragmentContainer,
                        type.getFragment()
                    )
                }
                binding.mainAppBarLayout.setExpanded(true)

                binding.mainTopTabs.visibility =
                    if (type == MainTabType.TALK || viewModel.currentContentsList.value == null) View.GONE else View.VISIBLE
                binding.mainFloatingButton.visibility =
                    if (type == MainTabType.TALK) View.GONE else View.VISIBLE
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
            ?.retry(3)
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
