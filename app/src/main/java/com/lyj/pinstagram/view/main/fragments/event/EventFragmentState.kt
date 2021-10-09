package com.lyj.pinstagram.view.main.fragments.event

import com.lyj.data.source.remote.entity.event.EventRetreiveResponse
import com.lyj.domain.model.network.event.EventModel


sealed interface EventFragmentState {
    object Init : EventFragmentState

    class Success(val response: List<EventModel>) : EventFragmentState

    object Fail : EventFragmentState

    object Empty : EventFragmentState
}