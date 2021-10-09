package com.lyj.domain.model.network.auth

data class JwtModel(
    val id : Int?,
    val name : String?,
    val email : String?
){
    val isValidated : Boolean
        get() = id != null && name != null && email != null
}