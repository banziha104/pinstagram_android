package com.lyj.domain.repository.network

import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.auth.SignInRequestModel
import com.lyj.domain.model.network.auth.SignResponseModel
import com.lyj.domain.model.network.auth.SignUpRequestModel
import io.reactivex.rxjava3.core.Single

interface AuthRepository {
    fun signIn(signInRequestModel: SignInRequestModel): Single<ApiModel<SignResponseModel>>
    fun signUp(signUpRequestModel: SignUpRequestModel): Single<ApiModel<SignResponseModel>>
}