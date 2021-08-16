package com.lyj.pinstagram.view.main.fragments.talk.adapter

import android.content.Context
import com.lyj.api.jwt.JwtAuthData
import com.lyj.api.socket.TalkMessage
import com.lyj.core.base.AdapterViewModel

class TalkAdapterViewModel(
    override var items: MutableList<TalkMessage>,
    override val context: Context,
) : AdapterViewModel<TalkMessage>{
    var authData: JwtAuthData? = null
}

operator fun TalkAdapterViewModel?.plusAssign(message : TalkMessage) {
    this?.items?.add(message)
}