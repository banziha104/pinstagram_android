package com.lyj.pinstagram.view.main

import android.app.Activity
import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Location
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyj.api.database.LocalDatabase
import com.lyj.api.network.contents.ContentsService
import com.lyj.api.storage.StorageUploader
import com.lyj.domain.base.ApiResponse
import com.lyj.domain.localdb.TOKEN_ID
import com.lyj.domain.localdb.TokenEntity
import com.lyj.domain.network.contents.response.ContentsRetrieveResponse
import com.lyj.pinstagram.R
import com.lyj.pinstagram.location.GeoCodeManager
import com.lyj.pinstagram.location.LocationEventManager
import com.lyj.pinstagram.location.protocol.ReverseGeoCoder
import com.lyj.pinstagram.view.main.fragments.home.HomeFragment
import com.lyj.pinstagram.view.main.fragments.map.MapFragment
import com.lyj.pinstagram.view.main.fragments.talk.TalkFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    application: Application,
    geoCodeManager: GeoCodeManager,
    val locationEventManager: LocationEventManager,
    val contentsService: ContentsService,
    val storageUploader: StorageUploader,
    val database: LocalDatabase
) : AndroidViewModel(application),
    ReverseGeoCoder by geoCodeManager {

    val originContentsList: MutableLiveData<List<ContentsRetrieveResponse>> by lazy {
        MutableLiveData<List<ContentsRetrieveResponse>>()
    }

    val currentContentsList: MutableLiveData<List<ContentsRetrieveResponse>> by lazy {
        MutableLiveData<List<ContentsRetrieveResponse>>()
    }

    val location: MutableLiveData<Address> by lazy {
        MutableLiveData<Address>()
    }

    fun requestContentsData(
        lat: Double,
        lng: Double
    ): Single<ApiResponse<List<ContentsRetrieveResponse>>> =
        contentsService.getByLocation("$lat,$lng")

    private val context: Context by lazy {
        getApplication<Application>().applicationContext

    }

    fun getUserLocation(activity: Activity) = locationEventManager.getUserLocationOnce(activity)

    fun getUserToken() :Single<List<TokenEntity>> = database.tokenDao().findToken().subscribeOn(Schedulers.io())

    fun getTokenObserve() : Flowable<List<TokenEntity>> = database.tokenDao().observeToken().subscribeOn(Schedulers.io())

    fun deleteToken() : Completable = database.tokenDao().delete(TOKEN_ID).subscribeOn(Schedulers.io())
}

enum class MainTabType(
    @StringRes val titleId: Int
) : MainTabContract {
    HOME(R.string.main_tap_home_title) {
        override fun getFragment(): Fragment = HomeFragment.instance
    },
    MAP(R.string.main_tap_map_title) {
        override fun getFragment(): Fragment = MapFragment.instance
    },
    TALK(R.string.main_tap_talk_title) {
        override fun getFragment(): Fragment = TalkFragment.instance
    },
}

interface MainTabContract {
    fun getFragment(): Fragment
}