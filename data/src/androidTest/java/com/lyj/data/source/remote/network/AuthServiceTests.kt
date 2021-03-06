package com.lyj.data.source.remote.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.data.source.remote.http.auth.AuthenticationService
import com.lyj.data.source.remote.entity.auth.request.SignInRequest
import com.lyj.data.source.remote.http.ApiBase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

// 별도의 테스트서버를 둘 예산이 없어서 생성, 수정, 삭제는 테스트 생략
@RunWith(AndroidJUnit4::class)
class AuthServiceTests : BaseServiceTest() {
    private lateinit var authService : AuthenticationService

    @Before
    fun `셋업`(){
        authService = serviceGenerator.generateService(AuthenticationService::class.java,okHttpClient,callAdapter,converterFactory)
    }

    @Test
    @Throws(Exception::class)
    fun `로그인`(){
        val request = SignInRequest("test@test.com","test")
        authService
            .signIn(request)
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValue{
                it.isOk && it.data?.token != null
            }
            .assertComplete()
    }
}