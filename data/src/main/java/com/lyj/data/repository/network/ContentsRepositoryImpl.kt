package com.lyj.data.repository.network

import com.lyj.data.mapper.ApiMapper
import com.lyj.data.mapper.ContentsMapper
import com.lyj.data.source.remote.http.contents.ContentsService
import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.contents.ContentsModel
import com.lyj.domain.model.network.contents.ContentsRequestModel
import com.lyj.domain.repository.network.ContentsRepository
import io.reactivex.rxjava3.core.Single

class ContentsRepositoryImpl(
    private val mapper: ContentsMapper,
    private val service: ContentsService
) : ContentsRepository {
    override suspend fun getById(id: Long): ApiModel<ContentsModel> =
        ApiMapper.toModel(service.getById(id))
    
    override fun getByLocation(latlng: String, limit: Int): Single<ApiModel<List<ContentsModel>>> = service
        .getByLocation(latlng,limit)
        .map(ApiMapper::toModel)

    override fun createContents(
        authorization: String,
        request: ContentsRequestModel
    ): Single<ApiModel<Long>> = service.createContents(authorization,mapper.toEntity(request)).map(ApiMapper::toModel)
}