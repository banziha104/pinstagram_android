package com.lyj.api.jwt

data class JwtAuthData(
    val id : Int?,
    val name : String?,
    val email : String?
){
    val isValidated : Boolean
        get() = id != null && name != null && email != null
}