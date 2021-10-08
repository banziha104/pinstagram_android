package com.lyj.pinstagram.view.main.fragments.event.layouts

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lyj.data.source.remote.entity.event.EventRetreiveResponse
import com.lyj.pinstagram.view.main.fragments.event.EventFragmentState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter


class EventMainPage(context: Context) : EventFragmentLayout<EventFragmentState.Success>(context) {

    @Composable
    @ExperimentalAnimationApi
    override fun View(state: EventFragmentState.Success) {
        var expaneded: Int? by remember { mutableStateOf(null) }
        val bottomNavigationPadding = 56.dp

        Surface(color = Color(0xFFE0E0E0)) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.padding(PaddingValues(bottom = bottomNavigationPadding)),
                ) {
                items(state.response.mapIndexed { index, eventRetrieveResponse -> index to eventRetrieveResponse }) { pair ->
                    EventCardView(
                        pair = pair,
                        expaneded != null && expaneded == pair.first
                    ) {
                        expaneded = if (it == expaneded) null else it
                    }
                }
            }

        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun EventCardView(
        pair: Pair<Int, EventRetreiveResponse>,
        isExpanded: Boolean,
        setExpandedIndex: (Int) -> Unit
    ) {
        val (index, response) = pair
        val image = rememberImagePainter(response.picture)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isExpanded) 400.dp else 300.dp)
                .clickable { setExpandedIndex(index) }
        ) {
            Column {
                Image(
                    painter = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                AnimatedVisibility(isExpanded, modifier = Modifier.height(100.dp)) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(
                            PaddingValues(vertical = 4.dp, horizontal = 8.dp)
                        )
                    ) {
                        Text(
                            text = response.title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = response.description,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = response.fullAddress,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
