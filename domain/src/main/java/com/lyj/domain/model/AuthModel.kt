package com.lyj.domain.model

import android.accounts.AccountAuthenticatorResponse

data class AuthModel(
    val email: String,
    val password: String,
    val name : String
)