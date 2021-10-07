package com.lyj.pinstagram.view.main.fragments.event

import com.lyj.data.source.local.temp.network.event.EventRetreiveResponse



sealed interface EventFragmentState {
    object Init : EventFragmentState

    class Success(val response: List<EventRetreiveResponse>) : EventFragmentState

    object Fail : EventFragmentState

    object Empty : EventFragmentState
}