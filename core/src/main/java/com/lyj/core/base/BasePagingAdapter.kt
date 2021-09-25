package com.lyj.core.base

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagingAdapter<DATA_SOURCE : Any, VIEW_HOLDER : RecyclerView.ViewHolder>(
    protected open val viewModel : PagingAdapterViewModel<DATA_SOURCE>,
) : PagingDataAdapter<DATA_SOURCE, VIEW_HOLDER>(viewModel.diffUtil){

}