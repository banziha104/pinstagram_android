package com.lyj.domain.model.network.auth

open class SignResponseModel(val token: String)

interface SignUpRequestModel{
    val email : String
    val password: String
    val name : String
}

interface SignInRequestModel {
    val email: String
    val password: String
}