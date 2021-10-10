package com.lyj.domain.usecase.network.contents

import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.contents.ContentsModel
import com.lyj.domain.repository.network.ContentsRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestContentsByLocationUseCase @Inject constructor(
    private val repository: ContentsRepository
) {
    fun execute(
        lat: Double,
        lng : Double,
        limit: Int = 1000
    ): Single<ApiModel<List<ContentsModel>>> = repository.getByLocation("$lat,$lng", limit)
        .subscribeOn(Schedulers.io())
}