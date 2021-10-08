package com.lyj.data.source.remote.entity.auth.request

data class SignUpRequest(
    val email: String,
    val password: String,
    val name : String
)