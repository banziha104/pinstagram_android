package com.lyj.core.extension.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

// 옵저버를 라이프사이클에 등록하는 익스텐션
operator fun Lifecycle.plusAssign(observer: LifecycleObserver) = this.addObserver(observer)
