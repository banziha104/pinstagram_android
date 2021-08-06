package com.lyj.pinstagram.view.main.fragments.home.adapter

import android.content.Context
import com.lyj.core.base.AdapterViewModel
import com.lyj.core.module.size.SizeMeasurable

class HomeGridViewModel(
    override val items: List<HomeGridItem>,
    override val context : Context
) : AdapterViewModel<HomeGridItem>, SizeMeasurable