package com.lyj.data.mapper

interface Mapper<ENTITY,MODEL> {
    fun toModel(entity : ENTITY) : MODEL
    fun toEntity(model : MODEL) : ENTITY
}