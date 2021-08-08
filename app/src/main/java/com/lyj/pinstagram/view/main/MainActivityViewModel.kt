package com.lyj.pinstagram.view.main

import android.app.Activity
import android.app.Application
import android.content.Context
import android.location.Address
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyj.api.network.contents.ContentsService
import com.lyj.domain.network.contents.response.ContentsRetrieveResponse
import com.lyj.pinstagram.R
import com.lyj.pinstagram.location.*
import com.lyj.pinstagram.location.protocol.ReverseGeoCoder
import com.lyj.pinstagram.view.main.fragments.home.HomeFragment
import com.lyj.pinstagram.view.main.fragments.map.MapFragment
import com.lyj.pinstagram.view.main.fragments.talk.TalkFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    application: Application,
    geoCodeManager: GeoCodeManager,
    private val locationEventManager: LocationEventManager,
    private val contentsService: ContentsService
) : AndroidViewModel(application),
    ReverseGeoCoder by geoCodeManager{

    val originContentsList : MutableLiveData<List<ContentsRetrieveResponse>> by lazy{
        MutableLiveData<List<ContentsRetrieveResponse>>()
    }

    val currentContentsList : MutableLiveData<List<ContentsRetrieveResponse>> by lazy{
        MutableLiveData<List<ContentsRetrieveResponse>>()
    }

    val location : MutableLiveData<Address> by lazy{
        MutableLiveData<Address>()
    }




    fun requestContentsData(lat : Double,lng : Double) = contentsService.getByLocation("$lat,$lng")

    private val context: Context by lazy {
        getApplication<Application>().applicationContext

    }

    val tabItems = MainTabType.values()



    fun getUserLocation(activity: Activity) = locationEventManager.getUserLocationOnce(activity)
}

enum class MainTabType(
    val titleId: Int
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