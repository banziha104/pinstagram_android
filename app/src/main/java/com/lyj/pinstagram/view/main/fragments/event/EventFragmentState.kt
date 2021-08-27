package com.lyj.pinstagram.view.main.fragments.event

import com.lyj.domain.network.event.EventRetreiveResponse


internal sealed interface EventFragmentState{
    class Init : EventFragmentState{

    }

    class Success(val response: List<EventRetreiveResponse>) : EventFragmentState{

    }

    class Fail() : EventFragmentState{

    }

    class Empty() : EventFragmentState{

    }
}