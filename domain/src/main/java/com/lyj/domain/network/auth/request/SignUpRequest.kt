package com.lyj.domain.network.auth.request

data class SignUpRequest(
    val email: String,
    val password: String,
    val name : String
)