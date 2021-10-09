package com.lyj.data.source.remote.entity.auth.request

import com.lyj.domain.model.network.auth.SignUpRequestModel

data class SignUpRequest(
    override val email: String,
    override val password: String,
    override val name: String
) : SignUpRequestModel