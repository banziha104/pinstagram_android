package com.lyj.domain.usecase

import com.lyj.domain.model.TokenModel
import com.lyj.domain.repository.local.TokenRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenUseCase @Inject constructor(
    val tokenRepository: TokenRepository
){
    fun findToken() : Single<List<TokenModel>> = tokenRepository.findToken()
    fun observeToken() : Flowable<List<TokenModel>> = tokenRepository.observeToken()
    fun insert(entity: TokenModel) : Completable = tokenRepository.refreshToken(entity)
    fun delete(id : Long = 1) : Completable = tokenRepository.delete()
}