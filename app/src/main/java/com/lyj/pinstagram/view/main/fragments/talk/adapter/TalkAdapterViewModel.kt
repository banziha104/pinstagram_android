package com.lyj.pinstagram.view.main.fragments.talk.adapter

import android.content.Context
import com.lyj.core.base.AdapterViewModel
import com.lyj.core.rx.DisposableLifecycleController
import com.lyj.domain.model.TalkModel
import com.lyj.domain.model.network.auth.JwtModel

class TalkAdapterViewModel(
    override var items: MutableList<TalkModel>,
    override val context: Context,
    val disposableContoller: DisposableLifecycleController,
) : AdapterViewModel<TalkModel>{
    var authData: JwtModel? = null
}

operator fun TalkAdapterViewModel?.plusAssign(message : TalkModel) {
    this?.items?.add(message)
}