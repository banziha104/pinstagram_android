package com.lyj.data.mapper

import com.lyj.data.mapper.base.EntityMapper
import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.domain.model.network.ApiModel



object ApiMapper {
    fun <ENTITY_DATA,MODEL_DATA> toEntity(model: ApiModel<MODEL_DATA>): ApiResponse<ENTITY_DATA> = ApiResponse(
        model.code,
        model.message,
        model.httpCode,
        model.data as? ENTITY_DATA
    )

    fun <ENTITY_DATA,MODEL_DATA> toModel(model: ApiResponse<ENTITY_DATA>):ApiModel<MODEL_DATA> = ApiModel(
        model.code,
        model.message,
        model.httpCode,
        model.data as? MODEL_DATA
    )
}
