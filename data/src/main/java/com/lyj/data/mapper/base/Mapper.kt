package com.lyj.data.mapper.base

interface EntityMapper<out ENTITY,in MODEL>  {
    fun toEntity(model: MODEL) : ENTITY
}

interface ModelMapper<in ENTITY,out MODEL> {
    fun toModel(entity: ENTITY) : MODEL
}