package com.lyj.pinstagram.view.main

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.lyj.pinstagram.R
import com.lyj.pinstagram.location.ContinuosLocationGetter
import com.lyj.pinstagram.location.LocationEventManager
import com.lyj.pinstagram.location.OneTimeLocationGetter
import com.lyj.pinstagram.view.main.fragments.home.HomeFragment
import com.lyj.pinstagram.view.main.fragments.map.MapFragment
import com.lyj.pinstagram.view.main.fragments.talk.TalkFragment
import com.lyj.pinstagram.view.main.fragments.user.UserFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    application: Application,
    locationEventManager: LocationEventManager
) : AndroidViewModel(application),
    ContinuosLocationGetter by locationEventManager,
    OneTimeLocationGetter by locationEventManager {

    private val context: Context by lazy {
        getApplication<Application>().applicationContext
    }

    val tabItems = MainTabType.values()

    fun getUserLocation(activity: Activity) = getUserLocationOnce(activity)
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
    USER(R.string.main_tap_user_title) {
        override fun getFragment(): Fragment = UserFragment.instance
    }
}

interface MainTabContract {
    fun getFragment(): Fragment
}