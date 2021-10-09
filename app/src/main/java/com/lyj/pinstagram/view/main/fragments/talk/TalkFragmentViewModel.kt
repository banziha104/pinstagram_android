package com.lyj.pinstagram.view.main.fragments.talk

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.lyj.data.source.remote.http.talk.TalkService
import com.lyj.data.source.remote.socket.SocketManager
import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.domain.model.TalkModel
import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.repository.network.SocketContract
import com.lyj.domain.usecase.local.token.FindTokenUseCase
import com.lyj.domain.usecase.network.socket.GetSocketContractUseCase
import com.lyj.domain.usecase.network.socket.RequestGetAllTalkMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TalkFragmentViewModel @Inject constructor(
    private val getSocketContractUseCase: GetSocketContractUseCase,
    private val requestGetAllTalkMessage: RequestGetAllTalkMessage
) : ViewModel() {

    fun createSocket(lifecycle: Lifecycle) : SocketContract = getSocketContractUseCase.execute(lifecycle)

    fun getAllTalkMessage() : Single<ApiModel<List<TalkModel>>> = requestGetAllTalkMessage.execute()
}