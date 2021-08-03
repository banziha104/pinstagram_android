package com.lyj.api.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.runner.RunWith

// 별도의 테스트서버를 둘 예산이 없어서 생성, 수정, 삭제는 테스트 생략
@RunWith(AndroidJUnit4::class)
class ContentsServiceTests : BaseServiceTest() {
    private lateinit var authService : ContentsServiceTests

    @Before
    fun `셋업`(){
        authService = ApiBase().generateService(ContentsServiceTests::class.java,okHttpClient,callAdapter,converterFactory)
    }

}