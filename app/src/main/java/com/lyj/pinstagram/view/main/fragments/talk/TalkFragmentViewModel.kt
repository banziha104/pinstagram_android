package com.lyj.pinstagram.view.main.fragments.talk

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.lyj.api.network.talk.TalkService
import com.lyj.api.socket.SocketContract
import com.lyj.api.socket.SocketManager
import com.lyj.api.socket.TalkMessage
import com.lyj.domain.base.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TalkFragmentViewModel @Inject constructor(
    private val socketFactory: SocketManager.Factory,
    private val talkService: TalkService
) : ViewModel() {

    fun createSocket(lifecycle: Lifecycle) : SocketContract = socketFactory.createSocket(lifecycle)

    fun getAllTalkMessage() : Single<ApiResponse<List<TalkMessage>>> = talkService.getAllMessage().subscribeOn(Schedulers.io())
}