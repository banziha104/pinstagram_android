package com.lyj.domain.usecase.network.contents

import com.lyj.domain.model.network.contents.ContentsRequestModel
import com.lyj.domain.repository.network.ContentsRepository
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestCreateContentsUseCase @Inject constructor(
    private val repository: ContentsRepository
) {
    fun execute(token: String, model: ContentsRequestModel) =
        repository.createContents(token, model).subscribeOn(Schedulers.io())
}