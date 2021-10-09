package com.lyj.data.source.remote.entity.auth.request

import com.lyj.domain.model.network.auth.SignUpRequestModel

class SignUpRequest(
    email: String,
    password: String,
    name : String
) : SignUpRequestModel(email, password, name)