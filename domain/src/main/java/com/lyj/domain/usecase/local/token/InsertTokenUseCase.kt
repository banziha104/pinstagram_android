package com.lyj.domain.usecase.local.token

import com.lyj.core.extension.lang.SchedulerType
import com.lyj.core.extension.lang.applyScheduler
import com.lyj.domain.model.TokenModel
import com.lyj.domain.repository.local.TokenRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    fun execute(entity: TokenModel): Completable =
        tokenRepository.refreshToken(entity).applyScheduler(subscribeOn = SchedulerType.IO)
}