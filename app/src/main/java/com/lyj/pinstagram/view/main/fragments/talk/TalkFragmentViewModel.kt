package com.lyj.pinstagram.view.main.fragments.talk

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.lyj.domain.model.network.talk.TalkModel
import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.repository.network.SocketContract
import com.lyj.domain.usecase.network.socket.GetSocketContractUseCase
import com.lyj.domain.usecase.network.socket.RequestGetAllTalkMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
class TalkFragmentViewModel @Inject constructor(
    private val getSocketContractUseCase: GetSocketContractUseCase,
    private val requestGetAllTalkMessage: RequestGetAllTalkMessage
) : ViewModel() {

    fun createSocket(lifecycle: Lifecycle) : SocketContract = getSocketContractUseCase.execute(lifecycle)

    fun getAllTalkMessage() : Single<ApiModel<List<TalkModel>>> = requestGetAllTalkMessage.execute()
}