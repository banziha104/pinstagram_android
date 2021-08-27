package com.lyj.pinstagram.view.main.fragments.event

import androidx.compose.material.Text
import androidx.compose.runtime.*
import com.lyj.pinstagram.databinding.EventFramgmentBinding
import kotlinx.coroutines.flow.Flow

@Composable
internal fun EventFragment.root(binding: EventFramgmentBinding, stateFlow : Flow<EventFragmentState>) {

    val eventState : State<EventFragmentState> = stateFlow.collectAsState(initial = EventFragmentState.Init() )

    when(eventState.value){
        is EventFragmentState.Init -> EventFragmentLayout.EmptyPage.view(eventState.value)
        is EventFragmentState.Empty -> EventFragmentLayout.InitPage.view(eventState.value)
        is EventFragmentState.Fail -> EventFragmentLayout.ErrorPage.view(eventState.value)
        is EventFragmentState.Success -> EventFragmentLayout.MainPage.view(eventState.value)
    }
}

internal sealed interface EventFragmentLayout {

    @Composable
    fun view(state : EventFragmentState)


    object MainPage : EventFragmentLayout {
        @Composable
        override fun view(state : EventFragmentState) {
            Text(text = "메인")
        }
    }

    object InitPage : EventFragmentLayout{
        @Composable
        override fun view(state: EventFragmentState) {
            Text(text = "초기화")
        }

    }
    object ErrorPage : EventFragmentLayout {
        @Composable
        override fun view(state : EventFragmentState) {
            Text(text = "에러").apply {

            }
        }
    }

    object EmptyPage : EventFragmentLayout {
        @Composable
        override fun view(state : EventFragmentState) {
            Text(text = "비어있음")
        }
    }
}

