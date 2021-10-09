package com.lyj.data.source.remote.entity

import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.ApiResponseCode


class ApiResponse<T>(
    code: String,
    message: String,
    httpCode: Int,
    data: T? = null
) : ApiModel<T>(code, message, httpCode, data){
    fun <DATA> copyWith(data : DATA) : ApiResponse<DATA> = ApiResponse(code, message, httpCode, data)
}