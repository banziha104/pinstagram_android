package com.lyj.domain.repository.network

import com.lyj.domain.model.network.auth.JwtModel
import io.jsonwebtoken.Claims

interface JwtRepository {
    fun parseJwt(token: String?): JwtModel?
    fun getClaims(token: String?): Claims?
}