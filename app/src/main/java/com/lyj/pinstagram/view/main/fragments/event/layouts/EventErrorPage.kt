package com.lyj.pinstagram.view.main.fragments.event.layouts

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lyj.pinstagram.R
import com.lyj.pinstagram.view.main.fragments.event.EventFragmentState


class EventErrorPage(context: Context) : EventFragmentLayout<EventFragmentState.Fail>(context) {
    @Composable
    override fun View(state: EventFragmentState.Fail) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = resString(R.string.event_fragment_fail_state), fontSize = 20.sp,fontWeight = FontWeight.Bold)
        }
    }

}