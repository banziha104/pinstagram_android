package com.lyj.pinstagram.view.main.fragments.talk.adapter

import android.content.Context
import com.lyj.data.source.remote.socket.TalkMessage
import com.lyj.core.base.AdapterViewModel
import com.lyj.core.rx.DisposableScopes
import com.lyj.domain.model.network.auth.JwtModel

class TalkAdapterViewModel(
    override var items: MutableList<TalkMessage>,
    override val context: Context,
    override val scopes: DisposableScopes,
) : AdapterViewModel<TalkMessage>{
    var authData: JwtModel? = null
}

operator fun TalkAdapterViewModel?.plusAssign(message : TalkMessage) {
    this?.items?.add(message)
}