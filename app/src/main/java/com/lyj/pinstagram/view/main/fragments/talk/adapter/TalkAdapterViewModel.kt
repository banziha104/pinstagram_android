package com.lyj.pinstagram.view.main.fragments.talk.adapter

import android.content.Context
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.iyeongjoon.nicname.core.rx.DisposableAddable
import com.lyj.api.jwt.JwtAuthData
import com.lyj.api.socket.TalkMessage
import com.lyj.core.base.AdapterViewModel
import io.reactivex.rxjava3.disposables.Disposable

class TalkAdapterViewModel(
    override var items: MutableList<TalkMessage>,
    val context: Context,
) : AdapterViewModel<TalkMessage>{
    var authData: JwtAuthData? = null
}

operator fun TalkAdapterViewModel?.plusAssign(message : TalkMessage) {
    this?.items?.add(message)
}