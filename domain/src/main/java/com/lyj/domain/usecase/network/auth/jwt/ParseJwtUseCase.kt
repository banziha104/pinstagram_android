package com.lyj.domain.usecase.network.auth.jwt

import com.lyj.domain.model.network.auth.JwtModel
import com.lyj.domain.repository.network.JwtRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParseJwtUseCase @Inject constructor(
    private val jwtRepository: JwtRepository
) {
    fun execute(token: String?): JwtModel? = jwtRepository.parseJwt(token)
}