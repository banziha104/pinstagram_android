package com.lyj.api.network

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.api.network.contents.ContentsService
import com.lyj.core.extension.testTag
import com.lyj.domain.network.contents.ContentsTagType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception
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
    fun `ID_조회`(){
        contentsService
            .getById(2)
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValue{
                it.isOk && it.data != null && it.data?.tag == ContentsTagType.FOOD
            }
            .assertComplete()
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