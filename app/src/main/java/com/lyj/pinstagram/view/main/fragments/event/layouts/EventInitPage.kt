package com.lyj.pinstagram.view.main.fragments.event.layouts

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lyj.pinstagram.R
import com.lyj.pinstagram.view.main.fragments.event.EventFragmentState

class EventInitPage(context: Context) : EventFragmentLayout<EventFragmentState.Init>(context) {

    private val animationSize = 125.dp

    @Composable
    override fun View(state: EventFragmentState.Init) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.progress_bar))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(composition, modifier = Modifier.size(animationSize))
            Text(text = resString(R.string.event_fragment_init_state), fontSize = 20.sp,fontWeight = FontWeight.Bold)
        }
    }

}
