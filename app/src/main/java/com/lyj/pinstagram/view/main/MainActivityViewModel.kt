package com.lyj.pinstagram.view.main

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.lyj.data.source.local.LocalDatabase
import com.lyj.data.common.jwt.JwtAuthData
import com.lyj.data.source.remote.http.contents.ContentsService
import com.lyj.data.source.remote.http.geo.GeometryService
import com.lyj.data.source.remote.storage.StorageUploader
import com.lyj.core.extension.android.resString
import com.lyj.core.extension.lang.testTag
import com.lyj.data.common.jwt.JwtManager
import com.lyj.data.source.local.entity.TOKEN_ID
import com.lyj.data.source.local.entity.TokenEntity
import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.data.source.remote.entity.contents.response.ContentsRetrieveResponse
import com.lyj.pinstagram.R
import com.lyj.pinstagram.location.GeoCodeManager
import com.lyj.pinstagram.location.LocationEventManager
import com.lyj.pinstagram.location.protocol.ReverseGeoCoder
import com.lyj.pinstagram.view.main.fragments.event.EventFragment
import com.lyj.pinstagram.view.main.fragments.home.HomeFragment
import com.lyj.pinstagram.view.main.fragments.map.MapFragment
import com.lyj.pinstagram.view.main.fragments.talk.TalkFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
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
    private val geometrySerivce: GeometryService,
    private val database: LocalDatabase,
    private val jwtManager: JwtManager,
) : AndroidViewModel(application),
    ReverseGeoCoder by geoCodeManager {

    val originContentsList: MutableLiveData<List<ContentsRetrieveResponse>> by lazy {
        MutableLiveData<List<ContentsRetrieveResponse>>()
    }

    val currentContentsList: MutableLiveData<List<ContentsRetrieveResponse>> by lazy {
        MutableLiveData<List<ContentsRetrieveResponse>>()
    }

    val currentAuthData: MutableLiveData<JwtAuthData?> by lazy {
        MutableLiveData<JwtAuthData?>()
    }

    val currentLocation: MutableLiveData<LatLng> by lazy {
        MutableLiveData<LatLng>()
    }

    val currentLocationName : MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }

    fun requestContentsData(
        lat: Double,
        lng: Double
    ): Single<ApiResponse<List<ContentsRetrieveResponse>>> =
        contentsService.getByLocation("$lat,$lng")

    fun requestGeometry(lat : Double, lng : Double): Single<String> =
        geometrySerivce
            .getReverseGeocoding("$lat,$lng")
            .map {
                if (it.isOk && it.data != null){
                    val component = it.data?.results?.get(0)?.addressComponents
                    val city = component?.firstOrNull {
                        it?.longName?.endsWith("시") ?: false
                    }?.longName ?: ""
                    val province = component?.firstOrNull { it?.longName?.endsWith("구") ?: false }?.longName ?: ""
                    val village = component?.firstOrNull { it?.longName?.endsWith("동") ?: false }?.longName ?: ""
                    Log.d(testTag,"$city $province $village")
                    "$city $province $village"
                }else{
                    resString(R.string.main_fail_address)
                }
            }

    fun parseToken(token: TokenEntity): JwtAuthData = jwtManager.parseJwt(token.token)

    fun getUserLocation(activity: Activity): Single<Pair<LatLng, ApiResponse<List<ContentsRetrieveResponse>>>>?{
        val location = currentLocation.value
        return if (location != null){
            Single.zip(Single.just(location),requestBySpecificLocation(activity,location.latitude,location.longitude)){a,b -> a to b}
        }else{
            locationEventManager.getUserLocationOnce(activity)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(Schedulers.io())
                ?.flatMap {
                    Single.zip(
                        Single.just(LatLng(it.latitude,it.longitude)),
                        requestContentsData(it.latitude, it.longitude)
                    ) { a, b -> a to b }
                }
                ?.retry(3)
                ?.observeOn(AndroidSchedulers.mainThread())
        }
    }


    fun requestBySpecificLocation(
        activity: Activity,
        lat: Double,
        lng: Double
    ): Single<ApiResponse<List<ContentsRetrieveResponse>>> =
        requestContentsData(lat, lng).subscribeOn(Schedulers.io()).retry(3)


    fun getUserToken(): Single<List<TokenEntity>> =
        database.tokenDao().findToken().subscribeOn(Schedulers.io())

    fun getTokenObserve(): Flowable<List<TokenEntity>> =
        database.tokenDao().observeToken().subscribeOn(Schedulers.io())

    fun deleteToken(): Completable =
        database.tokenDao().delete(TOKEN_ID).subscribeOn(Schedulers.io())
}

enum class MainTabType(
    @StringRes val titleId: Int
) : MainTabContract {
    HOME(R.string.main_tap_home_title) {
        override fun getFragment(): Fragment = HomeFragment()
    },
    MAP(R.string.main_tap_map_title) {
        override fun getFragment(): Fragment = MapFragment()
    },
    EVENT(R.string.main_tap_event_title) {
        override fun getFragment(): Fragment = EventFragment()
    },
    TALK(R.string.main_tap_talk_title) {
        override fun getFragment(): Fragment = TalkFragment()
    },
}

interface MainTabContract {
    fun getFragment(): Fragment
}