package com.lyj.domain.usecase.local.token

import com.lyj.domain.model.TokenModel
import com.lyj.domain.repository.local.TokenRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    fun execute(entity: TokenModel): Completable =
        tokenRepository.refreshToken(entity).subscribeOn(Schedulers.io())
}