package com.lyj.pinstagram.view.main.fragments.home.adapter

import android.content.Context
import com.lyj.core.base.AdapterViewModel
import com.lyj.core.module.size.SizeMeasurable
import com.lyj.customui.dialog.extension.getDimen
import com.lyj.pinstagram.R
import com.lyj.pinstagram.view.main.fragments.home.HomeFragment

class HomeGridViewModel(
    override val items: List<HomeGridItem>,
    override val context : Context
) : AdapterViewModel<HomeGridItem>, SizeMeasurable {
    val height : Int = (pxWidth - context.getDimen(R.dimen.home_gridview_horizontal_padding) * 2).toInt() / HomeFragment.NUMBER_OF_COLUMNS
}