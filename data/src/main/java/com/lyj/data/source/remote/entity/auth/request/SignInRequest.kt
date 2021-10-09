package com.lyj.data.source.remote.entity.auth.request

import com.lyj.domain.model.network.auth.SignInRequestModel

data class SignInRequest(
    override val email: String,
    override val password: String,
) : SignInRequestModel