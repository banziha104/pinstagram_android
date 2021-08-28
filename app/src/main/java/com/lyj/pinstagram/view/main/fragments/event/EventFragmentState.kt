package com.lyj.pinstagram.view.main.fragments.event

import com.lyj.domain.network.event.EventRetreiveResponse



sealed interface EventFragmentState {
    object Init : EventFragmentState

    class Success(val response: List<EventRetreiveResponse>) : EventFragmentState

    object Fail : EventFragmentState

    object Empty : EventFragmentState
}