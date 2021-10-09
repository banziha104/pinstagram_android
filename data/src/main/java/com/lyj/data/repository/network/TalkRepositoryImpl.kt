package com.lyj.data.repository.network

import com.lyj.data.mapper.ApiMapper
import com.lyj.data.source.remote.http.talk.TalkService
import com.lyj.domain.model.TalkModel
import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.repository.network.SocketContract
import com.lyj.domain.repository.network.SocketFactory
import com.lyj.domain.repository.network.TalkRepository
import io.reactivex.rxjava3.core.Single

class TalkRepositoryImpl(
    private val service: TalkService,
    socketContract: SocketFactory,
) : TalkRepository, SocketFactory by socketContract {
    override fun getAllMessage(): Single<ApiModel<List<TalkModel>>> = service.getAllMessage().map(ApiMapper::toModel)
}