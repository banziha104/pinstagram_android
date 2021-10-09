package com.lyj.data.repository.network

import com.lyj.data.mapper.ApiMapper
import com.lyj.data.source.remote.entity.auth.request.SignInRequest
import com.lyj.data.source.remote.entity.auth.request.SignUpRequest
import com.lyj.data.source.remote.http.auth.AuthenticationService
import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.auth.SignInRequestModel
import com.lyj.domain.model.network.auth.SignResponseModel
import com.lyj.domain.model.network.auth.SignUpRequestModel
import com.lyj.domain.repository.network.AuthRepository
import io.reactivex.rxjava3.core.Single

class AuthRepositoryImpl(
    val service: AuthenticationService,
) : AuthRepository {
    override fun signIn(signInRequestModel: SignInRequestModel): Single<ApiModel<SignResponseModel>> =
        service.signIn(signInRequestModel as SignInRequest).map(ApiMapper::toModel)


    override fun signUp(signUpRequestModel: SignUpRequestModel): Single<ApiModel<SignResponseModel>> =
        service.signUp(signUpRequestModel as SignUpRequest).map(ApiMapper::toModel)
}