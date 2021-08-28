package com.lyj.pinstagram.view.main.fragments.event.layouts

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import com.lyj.pinstagram.compose.ComposeLayoutContract
import com.lyj.pinstagram.extension.android.getDimen
import com.lyj.pinstagram.view.main.fragments.event.*
import kotlinx.coroutines.flow.Flow

@ExperimentalAnimationApi
@Composable
internal fun EventFragment.View(context: Context, stateFlow: Flow<EventFragmentState>) {

    val eventState: State<EventFragmentState> =
        stateFlow.collectAsState(initial = EventFragmentState.Init)
    var value = eventState.value

    when (value) {
        is EventFragmentState.Init -> EventInitPage(context).View(value)
        is EventFragmentState.Empty -> EventEmptyPage(context).View(value)
        is EventFragmentState.Fail -> EventErrorPage(context).View(value)
        is EventFragmentState.Success -> EventMainPage(context).View(value)
    }
}

sealed class EventFragmentLayout<T : EventFragmentState>(override val context: Context) : EventLayoutContract<T>
interface EventLayoutContract<T : EventFragmentState> : ComposeLayoutContract<T>