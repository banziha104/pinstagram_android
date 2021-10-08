package com.lyj.data.source.remote.http.auth

import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.data.source.remote.entity.auth.request.SignInRequest
import com.lyj.data.source.remote.entity.auth.request.SignUpRequest
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