package com.lyj.domain.usecase.network.socket

import androidx.lifecycle.Lifecycle
import com.lyj.domain.repository.network.SocketContract
import com.lyj.domain.repository.network.TalkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSocketContractUseCase @Inject constructor(
    private val repository: TalkRepository
) {
    fun execute(lifecycle: Lifecycle): SocketContract = repository.createSocket(lifecycle)
}
