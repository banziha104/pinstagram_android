package com.lyj.core.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<DATA_SOURCE, VIEW_HOLDER : RecyclerView.ViewHolder>(
    internal open val viewModel : AdapterViewModel<DATA_SOURCE>,
) : RecyclerView.Adapter<VIEW_HOLDER>()
