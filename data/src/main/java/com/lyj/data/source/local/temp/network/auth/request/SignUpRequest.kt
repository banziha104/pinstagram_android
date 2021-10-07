package com.lyj.data.source.local.temp.network.auth.request

data class SignUpRequest(
    val email: String,
    val password: String,
    val name : String
)