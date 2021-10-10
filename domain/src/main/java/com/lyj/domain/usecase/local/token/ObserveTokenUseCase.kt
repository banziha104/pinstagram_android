package com.lyj.domain.usecase.local.token

import com.lyj.domain.model.TokenModel
import com.lyj.domain.repository.local.TokenRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    fun execute(): Flowable<List<TokenModel>> =
        tokenRepository.observeToken().subscribeOn(Schedulers.io())
}