package com.lyj.pinstagram.view.main.fragments.event

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


sealed interface EventFragmentLayout : ComposeContract{
    object Root : EventFragmentLayout  {
        @Composable
        override fun view() {
            Text(text = "김텍스트")
        }
    }
}

interface ComposeContract{
    @Composable
    fun view()
}