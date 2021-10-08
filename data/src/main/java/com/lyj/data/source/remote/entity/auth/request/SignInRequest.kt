package com.lyj.data.source.remote.entity.auth.request


data class SignInRequest(
    val email: String,
    val password: String,
)