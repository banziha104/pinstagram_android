package com.lyj.pinstagram.view.main.fragments.home.adapter

import android.content.Context
import com.lyj.core.base.AdapterViewModel
import com.lyj.core.extension.base.resDimen
import com.lyj.core.module.size.SizeMeasurable
import com.lyj.core.rx.DisposableScopes
import com.lyj.pinstagram.R
import com.lyj.pinstagram.view.main.fragments.home.HomeFragment

class HomeGridViewModel(
    override val items: List<HomeGridItem>,
    override val context : Context,
    override val scopes: DisposableScopes,
    val onItemClick : (HomeGridItem) -> Unit
) : AdapterViewModel<HomeGridItem>, SizeMeasurable {
    val height : Int = (pxWidth - resDimen(R.dimen.home_gridview_horizontal_padding) * 2).toInt() / HomeFragment.NUMBER_OF_COLUMNS
}