package com.lyj.domain.usecase.network.socket

import com.lyj.domain.model.TalkModel
import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.repository.network.TalkRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestGetAllTalkMessage @Inject constructor(
    private val repository: TalkRepository
) {
    fun execute(): Single<ApiModel<List<TalkModel>>> = repository.getAllMessage()
}