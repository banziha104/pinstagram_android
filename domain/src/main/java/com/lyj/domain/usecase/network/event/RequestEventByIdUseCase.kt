package com.lyj.domain.usecase.network.event

import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.event.EventModel
import com.lyj.domain.repository.network.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestEventByIdUseCase @Inject constructor(
    private val repository: EventRepository
) {
    fun execute(id: Long): Flow<ApiModel<EventModel>> = flow {
        emit(repository.getById(id))
    }
}