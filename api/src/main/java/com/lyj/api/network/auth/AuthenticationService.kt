package com.lyj.api.network.auth

import com.lyj.domain.base.ApiResponse
import com.lyj.domain.network.auth.request.SignInRequest
import com.lyj.domain.network.auth.request.SignUpRequest
import com.lyj.domain.network.auth.response.SignInResponse
import com.lyj.domain.network.auth.response.SignUpResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("auth/signin")
    fun signIn(@Body request: SignInRequest) : Single<ApiResponse<SignInResponse>>

    @POST("auth/signup")
    fun signUp(@Body request: SignUpRequest) : Single<ApiResponse<SignUpResponse>>
}