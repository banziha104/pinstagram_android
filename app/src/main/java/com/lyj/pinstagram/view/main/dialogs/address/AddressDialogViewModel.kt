package com.lyj.pinstagram.view.main.dialogs.address

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.lyj.api.network.geo.GeometrySerivce
import com.lyj.core.module.size.SizeMeasurable
import com.lyj.domain.base.ApiResponse
import com.lyj.domain.network.geo.geocoding.GeoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class AddressDialogViewModel @Inject constructor(
    application: Application,
    private val geometryService: GeometrySerivce,
) : AndroidViewModel(application), SizeMeasurable {
    override val context: Context by lazy { getApplication<Application>().applicationContext }

    fun requestGeocoding(address : String ) : Single<ApiResponse<GeoResponse>> = geometryService.getGeocoding(address).subscribeOn(Schedulers.io())
}