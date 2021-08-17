package com.lyj.api.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.api.network.talk.TalkService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class TalkServiceTests : BaseServiceTest() {
    private lateinit var talkService: TalkService

    @Before
    fun `셋업`(){
        talkService = ApiBase().generateService(TalkService::class.java,okHttpClient,callAdapter,converterFactory)
    }

    @Test
    @Throws(Exception::class)
    fun `전체_메세지_가져오기`(){
        talkService
            .getAllMessage()
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValue{
                it.isOk && it.data != null && it.data?.isNotEmpty() == true
            }
            .assertComplete()
    }
}