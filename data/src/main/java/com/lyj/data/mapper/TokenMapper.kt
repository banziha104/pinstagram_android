package com.lyj.data.mapper

import com.lyj.data.source.local.entity.TokenEntity
import com.lyj.domain.model.TokenModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenMapper @Inject constructor() : Mapper<TokenEntity,TokenModel> {
    override fun toModel(entity: TokenEntity): TokenModel = TokenModel(entity.id,entity.token)

    override fun toEntity(model: TokenModel): TokenEntity = TokenEntity(model.id,model.token)
}