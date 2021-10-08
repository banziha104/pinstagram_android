package com.lyj.domain.repository.local

import com.lyj.domain.model.TokenModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single


interface TokenRepository : TokenDaoContract

interface TokenDaoContract{
    fun findToken() : Single<List<TokenModel>>
    fun observeToken() : Flowable<List<TokenModel>>
    fun refreshToken(entity: TokenModel) : Completable
    fun delete(id : Long = 1) : Completable
}