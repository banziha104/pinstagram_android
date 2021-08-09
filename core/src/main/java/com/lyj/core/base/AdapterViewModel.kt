package com.lyj.core.base

interface AdapterViewModel<T> {
    val items : Collection<T>

    val itemCount : Int
    get() = items.size
}