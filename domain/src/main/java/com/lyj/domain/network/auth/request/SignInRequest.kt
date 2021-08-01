package com.lyj.domain.network.auth.request


data class SignInRequest(
    val email: String,
    val password: String,
)