package com.lyj.data.repository.local

import com.lyj.data.mapper.TokenMapper
import com.lyj.data.source.local.dao.TokenDao
import com.lyj.domain.model.TokenModel
import com.lyj.domain.repository.local.TokenRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single


class TokenRepositoryImpl(
    private val mapper: TokenMapper,
    private val dao: TokenDao
) : TokenRepository {
    override fun findToken(): Single<List<TokenModel>> =
        dao.findToken().map { list -> list.map { mapper.toModel(it) } }

    override fun observeToken(): Flowable<List<TokenModel>> =
        dao.observeToken().map { list -> list.map { mapper.toModel(it) } }

    override fun refreshToken(entity: TokenModel): Completable = dao.insert(mapper.toEntity(entity))

    override fun delete(id: Long): Completable = dao.delete(id)
}