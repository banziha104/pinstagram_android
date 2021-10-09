package com.lyj.pinstagram.view.main.fragments.event

import androidx.lifecycle.ViewModel
import com.lyj.data.source.remote.http.event.EventService
import com.lyj.domain.usecase.network.event.RequestEventByLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EventFragmentViewModel @Inject constructor(
    private val requestEventByLocationUseCase: RequestEventByLocationUseCase
) : ViewModel() {

    internal fun getEventState(
        lat: Double,
        lng: Double
    ): Flow<EventFragmentState> =
        requestEventByLocationUseCase
            .execute("$lat,$lng")
            .map {
                if (!it.isOk) {
                    EventFragmentState.Fail
                } else {
                    if (it.data != null && it.data!!.isNotEmpty()) {
                        EventFragmentState.Success(it.data!!)
                    } else {
                        EventFragmentState.Empty
                    }
                }
            }.catch {
                it.printStackTrace()
            }
}