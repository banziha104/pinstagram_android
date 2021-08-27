package com.lyj.pinstagram.view.main.fragments.event

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.lyj.api.network.event.EventService
import com.lyj.domain.base.ApiResponse
import com.lyj.domain.network.event.EventRetreiveResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventFragmentViewModel @Inject constructor(
    private val eventService: EventService
) : ViewModel() {

    internal fun getEventState(
        lat: Double,
        lng: Double
    ): Flow<EventFragmentState> =
        flow {
            emit(eventService.getByLocation("$lat,$lng"))
        }.map {
            if (!it.isOk){
                EventFragmentState.Fail()
            }else{
                if (it.data != null && it.data!!.isNotEmpty()){
                    EventFragmentState.Success(it.data!!)
                }else{
                    EventFragmentState.Empty()
                }
            }

        }.catch {
            it.printStackTrace()
        }

}