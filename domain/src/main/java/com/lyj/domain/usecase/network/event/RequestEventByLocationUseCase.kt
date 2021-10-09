package com.lyj.domain.usecase.network.event

import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.event.EventModel
import com.lyj.domain.repository.network.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestEventByLocationUseCase @Inject constructor(
    private val repository: EventRepository
) {
    fun execute(latlng: String, limit: Int = 1000): Flow<ApiModel<List<EventModel>>> = flow {
        emit(repository.getByLocation(latlng, limit))
    }
}