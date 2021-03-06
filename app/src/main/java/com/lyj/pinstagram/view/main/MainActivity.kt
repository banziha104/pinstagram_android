package com.lyj.pinstagram.view.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.commit
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.lyj.core.extension.android.*
import com.lyj.core.extension.lang.testTag
import com.lyj.core.rx.*
import com.lyj.domain.model.network.contents.ContentsTagType
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.ActivityMainBinding
import com.lyj.pinstagram.extension.android.TabLayoutEventType
import com.lyj.pinstagram.extension.android.selectedObserver
import com.lyj.pinstagram.view.ProgressController
import com.lyj.pinstagram.view.main.dialogs.address.AddressDialog
import com.lyj.pinstagram.view.main.dialogs.sign.SignDialog
import com.lyj.pinstagram.view.main.dialogs.write.WriteDialog
import com.lyj.pinstagram.view.main.fragments.talk.TalkSendContact
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

typealias SetCurrentLocation = (Double, Double) -> Unit

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    TalkSendContact,
    ProgressController,
    RequestChangeCurrentLocation,
    DisposableLifecycleController
{

    private val viewModel: MainActivityViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override val disposableLifecycleObserver: RxLifecycleObserver = RxLifecycleObserver(this)

    override val editTextObserver: Observable<String> by lazy {
        binding
            .mainEditTalk
            .editText
            .textChanges()
            .map { it.toString() }
    }

    override val btnSendObserver: Observable<Unit> by lazy {
        binding
            .mainBtnTalkSend
            .clicks()
    }

    override val clearText: () -> Unit = {
        binding
            .mainEditTalk
            .editText
            .setText("")
    }

    override val progressLayout: View by lazy {
        binding.mainProgressLayout
    }

    private val controlledView: Collection<View> by lazy {
        listOf(
            binding.mainBtnTalkSend,
            binding.mainTabLayoutTop,
            binding.mainBtnFloating,
            binding.mainBtnAuth,
            binding.mainBottomNavigation
        )
    }

    private val setCurrentLocation: SetCurrentLocation = { lat, lng ->
        viewModel.currentLocation.postValue(LatLng(lat, lng))
    }

    override fun showProgressLayout(controlledViews: Collection<View>?) {
        super.showProgressLayout(controlledViews)
        binding.mainBottomNavigation.menu.forEach { it.isEnabled = false }
    }

    override fun hideProgressLayout(controlledViews: Collection<View>?) {
        super.hideProgressLayout(controlledViews)
        binding.mainBottomNavigation.menu.forEach { it.isEnabled = true }
    }


    override fun requestCurrentLocation(lat: Double, lng: Double) {
        viewModel.currentLocation.postValue(LatLng(lat, lng))
    }

    private var tabType: MainTabType = MainTabType.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showProgressLayout(controlledView)
        observeEvent()
        observeLiveData()
    }

    private fun observeEvent() {
        observeOnceUserLocation()
        observeToken()
        observeTopTabSelected()
        observeBottomTabSelected()
        observeFloatingButton()
        observeAuthButton()
        observeLocationText()
    }

    private fun observeLiveData() {
        viewModel.currentLocation.observe(this) { it ->
            viewModel
                .requestReversedGeoCodeUseCase
                .execute(it.latitude, it.longitude)
                .observeOn(AndroidSchedulers.mainThread())
                .disposeByOnDestory(this)
                .subscribe({ address ->
                    if (address != null) {
                        binding.mainTxtAddress.text = address
                    } else {
                        resString(R.string.main_fail_address)
                    }
                }, {
                    binding.mainTxtAddress.text = resString(R.string.main_fail_address)
                    it.printStackTrace()
                })
            viewModel
                .requestBySpecificLocation(it.latitude, it.longitude)
                .doOnSubscribe { showProgressLayout() }
                .observeOn(AndroidSchedulers.mainThread())
                .disposeByOnDestory(this)
                .subscribe({ response ->
                    if (response.isOk && response.data != null) {
                        viewModel.originContentsList.postValue(response.data)
                        viewModel.currentContentsList.postValue(response.data)
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.main_network_warning),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    hideProgressLayout()
                }, {
                    Toast.makeText(
                        this,
                        resString(R.string.main_network_warning),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    hideProgressLayout()
                    it.printStackTrace()
                })

        }


        binding.mainTabLayoutTop.let { tabLayout ->
            viewModel.originContentsList.observe(this) { list ->
                val group = list.groupBy { it.tag }
                if (group.isEmpty()) {
                    tabLayout.visibility = View.GONE
                } else {
                    tabLayout.removeAllTabs()
                    tabLayout.addTab(
                        tabLayout.newTab().setText(resString(R.string.main_top_tab_all))
                    )
                    group.keys.forEach {
                        tabLayout.addTab(tabLayout.newTab().setText(resString(it.kor)))
                    }
                    tabLayout.visibility = View.VISIBLE
                }
                hideProgressLayout(controlledView)
            }
        }

    }

    private fun observeToken(){
        viewModel
            .observeTokenUseCase
            .execute()
            .observeOn(AndroidSchedulers.mainThread())
            .disposeByOnDestory(this)
            .subscribe({
                Log.d(testTag, "entity" + it.joinToString(","))
                val hasToken = it.isNotEmpty() && it.first().token.isNotBlank()
                viewModel.currentAuthData.value =
                    if (hasToken) viewModel.parseJwtUseCase.execute(it.first().token) else null
                binding.mainBtnAuth.setImageDrawable(
                    resDrawble(
                        if (hasToken) R.drawable.user_icon_login
                        else R.drawable.user_icon
                    )
                )
            }, {
                it.printStackTrace()
            })
    }

    private fun observeAuthButton(){
        binding
            .mainBtnAuth
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .flatMapSingle {
                viewModel
                    .findTokenUseCase
                    .execute()
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    SignDialog().show(supportFragmentManager, null)
                } else {
                    AlertDialog.Builder(this)
                        .setTitle(resString(R.string.main_logout_dialog_title))
                        .setMessage(resString(R.string.main_logout_dialog_content))
                        .setPositiveButton(resString(R.string.main_logout_dialog_positive)) { dialog, _ ->
                            viewModel
                                .deleteTokenUseCase
                                .execute()
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
                            resString(R.string.main_logout_dialog_negative)
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }, {
                Toast.makeText(this, R.string.main_logout_warning, Toast.LENGTH_LONG).show()
                it.printStackTrace()
            })
    }

    private fun observeFloatingButton(){

        binding
            .mainBtnFloating
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .disposeByOnDestory(this)
            .subscribe({
                val auth = viewModel.currentAuthData.value
                val latLng = viewModel.currentLocation.value
                if (auth != null && latLng != null) {
                    WriteDialog().show(supportFragmentManager, null)
                } else {
                    Toast.makeText(this, R.string.main_needs_auth, Toast.LENGTH_LONG).show()
                }
            }, {
                it.printStackTrace()
            })
    }

    private fun observeLocationText(){
        binding.mainTxtAddress
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .disposeByOnDestory(this)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                AddressDialog(setCurrentLocation).show(supportFragmentManager, null)
            }
    }

    private fun observeTopTabSelected(){
        binding
            .mainTabLayoutTop
            .selectedObserver()
            .filter { it == TabLayoutEventType.SELECTED }
            .observeOn(AndroidSchedulers.mainThread())
            .disposeByOnDestory(this)
            .subscribe({
                if (it.text != null) {
                    if (it.text == resString(R.string.main_top_tab_all)) {
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

    private fun observeBottomTabSelected(){
        binding
            .mainBottomNavigation
            .selectedObserver(this, default = MainTabType.HOME)
            .disposeByOnDestory(this)
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                when (it.title) {
                    resString(R.string.main_tap_home_title) -> MainTabType.HOME
                    resString(R.string.main_tap_map_title) -> MainTabType.MAP
                    resString(R.string.main_tap_talk_title) -> MainTabType.TALK
                    resString(R.string.main_tap_event_title) -> MainTabType.EVENT
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

                binding.mainTabLayoutTop.visibility =
                    if (type == MainTabType.TALK
                        || type == MainTabType.EVENT
                        || viewModel.currentContentsList.value == null
                        || viewModel.currentContentsList.value!!.isEmpty()
                    ) View.GONE else View.VISIBLE
                binding.mainBtnFloating.visibility =
                    if (type == MainTabType.TALK) View.GONE else View.VISIBLE
                binding.mainLayoutTalkSend.visibility =
                    if (type == MainTabType.TALK) View.VISIBLE else View.GONE
            }, {
                it.printStackTrace()
            })
    }


    private fun observeOnceUserLocation(){
        viewModel
            .getUserLocation(this)
            ?.disposeByOnDestory(this)
            ?.subscribe({ (location, response) ->
                viewModel.currentLocation.postValue(LatLng(location.latitude, location.longitude))
            }, {
                Snackbar.make(
                    binding.mainLayoutRoot,
                    R.string.main_location_warning,
                    Snackbar.LENGTH_LONG
                ).show()
                it.printStackTrace()
            })
    }
}


interface RequestChangeCurrentLocation {
    fun requestCurrentLocation(lat: Double, lng: Double)
}