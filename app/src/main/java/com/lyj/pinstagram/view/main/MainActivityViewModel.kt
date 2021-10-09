package com.lyj.pinstagram.view.main

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.lyj.data.source.remote.http.contents.ContentsService
import com.lyj.data.source.remote.http.geo.GeometryService
import com.lyj.data.source.remote.storage.StorageUploader
import com.lyj.core.extension.android.resString
import com.lyj.core.extension.lang.testTag
import com.lyj.data.source.local.entity.TOKEN_ID
import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.data.source.remote.entity.contents.response.ContentsRetrieveResponse
import com.lyj.pinstagram.R
import com.lyj.data.source.android.location.GeoCodeManager
import com.lyj.data.source.android.location.protocol.ReverseGeoCoder
import com.lyj.domain.model.TokenModel
import com.lyj.domain.model.network.auth.JwtModel
import com.lyj.domain.usecase.android.location.GetLocationUseCase
import com.lyj.domain.usecase.local.token.DeleteTokenUseCase
import com.lyj.domain.usecase.local.token.FindTokenUseCase
import com.lyj.domain.usecase.local.token.ObserveTokenUseCase
import com.lyj.domain.usecase.network.auth.jwt.ParseJwtUseCase
import com.lyj.domain.usecase.network.geo.RequestReversedGeoCodeUseCase
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
    private val getLocationUseCase: GetLocationUseCase,
    val findTokenUseCase: FindTokenUseCase,
    val observeTokenUseCase: ObserveTokenUseCase,
    val deleteTokenUseCase: DeleteTokenUseCase,
    val parseJwtUseCase: ParseJwtUseCase,
    val contentsService: ContentsService,
    val storageUploader: StorageUploader,
    private val requestReversedGeoCodeUseCase: RequestReversedGeoCodeUseCase
) : AndroidViewModel(application),
    ReverseGeoCoder by geoCodeManager {

    val originContentsList: MutableLiveData<List<ContentsRetrieveResponse>> by lazy {
        MutableLiveData<List<ContentsRetrieveResponse>>()
    }

    val currentContentsList: MutableLiveData<List<ContentsRetrieveResponse>> by lazy {
        MutableLiveData<List<ContentsRetrieveResponse>>()
    }

    val currentAuthData: MutableLiveData<JwtModel?> by lazy {
        MutableLiveData<JwtModel?>()
    }

    val currentLocation: MutableLiveData<LatLng> by lazy {
        MutableLiveData<LatLng>()
    }

    private fun requestContentsData(
        lat: Double,
        lng: Double
    ): Single<ApiResponse<List<ContentsRetrieveResponse>>> =
        contentsService.getByLocation("$lat,$lng")

    fun requestGeometry(lat: Double, lng: Double): Single<String> =
        requestReversedGeoCodeUseCase
            .execute("$lat,$lng")
            .map { response ->
                val data = response.data
                Log.d(testTag,"${data?.joinToString("/")}")
                if (response.isOk && data != null && data.isNotEmpty()) {
                    val city =
                        data.firstOrNull { it.address?.endsWith("시") ?: false }?.address ?: ""
                    val province =
                        data.firstOrNull { it.address?.endsWith("시") ?: false }?.address ?: ""
                    val village =
                        data.firstOrNull { it?.address?.endsWith("동") ?: false }?.address ?: ""
                    "$city $province $village"
                } else {
                    resString(R.string.main_fail_address)
                }
            }

    fun getUserLocation(activity: Activity): Single<Pair<LatLng, ApiResponse<List<ContentsRetrieveResponse>>>>? {
        val location = currentLocation.value
        return if (location != null) {
            Single.zip(
                Single.just(location),
                requestBySpecificLocation(location.latitude, location.longitude)
            ) { a, b -> a to b }
        } else {
            getLocationUseCase.execute(activity)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(Schedulers.io())
                ?.flatMap {
                    Single.zip(
                        Single.just(LatLng(it.latitude, it.longitude)),
                        requestContentsData(it.latitude, it.longitude)
                    ) { a, b -> a to b }
                }
                ?.retry(3)
                ?.observeOn(AndroidSchedulers.mainThread())
        }
    }


    fun requestBySpecificLocation(
        lat: Double,
        lng: Double
    ): Single<ApiResponse<List<ContentsRetrieveResponse>>> =
        requestContentsData(lat, lng).subscribeOn(Schedulers.io()).retry(3)
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