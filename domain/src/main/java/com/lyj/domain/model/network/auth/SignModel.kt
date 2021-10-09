package com.lyj.domain.model.network.auth

open class SignResponseModel(val token: String)
open class SignUpRequestModel(
    val email : String,
    val password: String,
    val name : String
)

interface SignInRequestModel {
    val email: String
    val password: String
}