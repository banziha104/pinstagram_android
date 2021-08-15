package com.lyj.pinstagram.view.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.commit
import com.iyeongjoon.nicname.core.rx.DisposableFunction
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.lyj.core.base.BaseActivity
import com.lyj.core.extension.lang.plusAssign
import com.lyj.core.extension.lang.testTag
import com.lyj.core.permission.PermissionManager
import com.lyj.domain.network.contents.ContentsTagType
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivityMainBinding
import com.lyj.pinstagram.extension.android.TabLayoutEventType
import com.lyj.pinstagram.extension.android.selectedObserver
import com.lyj.pinstagram.view.ProgressController
import com.lyj.pinstagram.view.main.dialogs.sign.SignDialog
import com.lyj.pinstagram.view.main.dialogs.sign.SignDialogViewModel
import com.lyj.pinstagram.view.main.dialogs.write.WriteDialog
import com.lyj.pinstagram.view.main.dialogs.write.WriteDialogViewModel
import com.lyj.pinstagram.view.main.fragments.talk.TalkSendContact
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BaseActivity<MainActivityViewModel, ActivityMainBinding>(R.layout.activity_main,
        { ActivityMainBinding.inflate(it) }), TalkSendContact , ProgressController{
    @Inject
    internal lateinit var permissionManager: PermissionManager

    override val viewModel: MainActivityViewModel by viewModels()

    override val editTextObserver: Observable<String> by lazy {
        binding
            .mainTalkEditText
            .editText
            .textChanges()
            .map { it.toString() }
    }

    override val btnSendObserver: Observable<Unit> by lazy {
        binding
            .mainTalkBtnSend
            .clicks()
    }

    override val clearText: () -> Unit = {
        binding
            .mainTalkEditText
            .editText
            .setText("")
    }

    override val progressLayout: View by lazy {
        binding.mainProgressLayout
    }

    override val controlledView: Collection<View> by lazy {
        listOf(
            binding.mainTalkBtnSend,
            binding.mainTopTabs,
            binding.mainFloatingButton,
            binding.btnMainAuth,
            binding.mainBottomNavigation
        )
    }

    override fun showProgressLayout() {
        super.showProgressLayout()
        binding.mainBottomNavigation.menu.forEach { it.isEnabled = false }
    }

    override fun hideProgressLayout() {
        super.hideProgressLayout()
        binding.mainBottomNavigation.menu.forEach { it.isEnabled = true }
    }

    private var tabType: MainTabType = MainTabType.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProgressLayout()
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
                    hideProgressLayout()
                }
            }
        }
    }

    private fun observeToken(): DisposableFunction = {
        viewModel
            .getTokenObserve()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(testTag,"${it[0].token}")
                val hasToken = it.isNotEmpty() && it.first().token.isNotBlank()
                viewModel.currentAuthData.value =
                    if (hasToken) viewModel.parseToken(it.first()) else null
                binding.btnMainAuth.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        if (hasToken) R.drawable.user_icon_login
                        else R.drawable.user_icon
                    )
                )
            }, {
                it.printStackTrace()
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
                    SignDialog().show(supportFragmentManager, null)
                } else {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.main_logout_dialog_title))
                        .setMessage(getString(R.string.main_logout_dialog_content))
                        .setPositiveButton(getString(R.string.main_logout_dialog_positive)) { dialog, _ ->
                            viewModel
                                .deleteToken()
                                .subscribe({
                                    viewModel.currentAuthData.postValue(null)
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
                val auth = viewModel.currentAuthData.value
                if (auth != null){
                    WriteDialog().show(supportFragmentManager, null)
                }else {
                    Toast.makeText(this,R.string.main_needs_auth,Toast.LENGTH_LONG).show()
                }
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
                binding.mainTalkSendLayout.visibility =
                    if (type == MainTabType.TALK) View.VISIBLE else View.GONE
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
