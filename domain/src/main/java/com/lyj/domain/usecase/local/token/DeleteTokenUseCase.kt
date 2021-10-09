package com.lyj.domain.usecase.local.token

import com.lyj.domain.repository.local.TokenRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    fun execute(id: Long = 1): Completable =
        tokenRepository.delete(id)
            .subscribeOn(Schedulers.io())
}