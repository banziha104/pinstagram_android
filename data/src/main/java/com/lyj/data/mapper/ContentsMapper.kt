package com.lyj.data.mapper

import com.lyj.data.mapper.base.EntityMapper
import com.lyj.data.source.remote.entity.contents.request.ContentsCreateRequest
import com.lyj.domain.model.network.contents.ContentsRequestModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentsMapper @Inject constructor() : EntityMapper<ContentsCreateRequest, ContentsRequestModel> {
    override fun toEntity(model: ContentsRequestModel): ContentsCreateRequest =
        model.run { ContentsCreateRequest(title, description, picture, tag, lat, lng) }
}