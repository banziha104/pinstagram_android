package com.lyj.data.source.remote.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.data.source.remote.http.ApiBase
import com.lyj.data.source.remote.http.contents.ContentsService
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

// 별도의 테스트서버를 둘 예산이 없어서 생성, 수정, 삭제는 테스트 생략
@RunWith(AndroidJUnit4::class)
class ContentsServiceTests : BaseServiceTest() {
    private lateinit var contentsService : ContentsService

    @Before
    fun `셋업`(){
        contentsService = ApiBase().generateService(ContentsService::class.java,okHttpClient,callAdapter,converterFactory)
    }

    @Test
    @Throws(Exception::class)
    fun `ID_조회`() =  runBlocking {
        val result = contentsService
            .getById(2)
        assert(result.isOk)
        assert(result.data != null)
    }

    @Test
    @Throws(Exception::class)
    fun `위도_경도_조회`(){
        contentsService
            .getByLocation("37.389019, 127.122628",1000)
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValue{
                it.isOk && it.data != null && it.data?.isNotEmpty() == true
            }
            .assertComplete()
    }

}