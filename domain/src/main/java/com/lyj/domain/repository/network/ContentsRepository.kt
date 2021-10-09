package com.lyj.domain.repository.network

import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.contents.ContentsModel
import com.lyj.domain.model.network.contents.ContentsRequestModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ContentsRepository {
    suspend fun getById(
        id : Long
    ) : ApiModel<ContentsModel>

    fun getByLocation(
        latlng : String,
        limit : Int = 1000
    ) : Single<ApiModel<List<ContentsModel>>>

    fun createContents(
        authorization : String,
        request: ContentsRequestModel
    ) : Single<ApiModel<Long>>
}