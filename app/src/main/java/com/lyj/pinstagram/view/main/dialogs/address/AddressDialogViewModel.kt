package com.lyj.pinstagram.view.main.dialogs.address

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.lyj.data.source.remote.http.geo.GeometryService
import com.lyj.core.module.size.SizeMeasurable
import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.data.source.remote.entity.geo.geocoding.GeoResponse
import com.lyj.domain.usecase.network.geo.RequestGeoCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class AddressDialogViewModel @Inject constructor(
    application: Application,
    val requestGeoCodeUseCase: RequestGeoCodeUseCase,
) : AndroidViewModel(application), SizeMeasurable {
    override val context: Context by lazy { getApplication<Application>().applicationContext }
}