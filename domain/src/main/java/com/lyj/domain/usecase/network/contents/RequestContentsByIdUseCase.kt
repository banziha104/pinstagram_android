package com.lyj.domain.usecase.network.contents

import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.contents.ContentsModel
import com.lyj.domain.repository.network.ContentsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestContentsByIdUseCaseUseCase @Inject constructor(
    private val repository: ContentsRepository
) {
    suspend fun execute(id : Long): ApiModel<ContentsModel> = repository.getById(id)
}