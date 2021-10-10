package com.lyj.domain.usecase.network.auth.sign

import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.auth.SignInRequestModel
import com.lyj.domain.model.network.auth.SignResponseModel
import com.lyj.domain.repository.network.AuthRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    fun execute(signInRequestModel: SignInRequestModel): Single<ApiModel<SignResponseModel>> = authRepository
        .signIn(signInRequestModel)
        .subscribeOn(Schedulers.io())
}