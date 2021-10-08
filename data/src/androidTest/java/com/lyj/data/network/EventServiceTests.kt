package com.lyj.data.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.data.source.remote.http.ApiBase
import com.lyj.data.source.remote.http.event.EventService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class EventServiceTests : BaseServiceTest(){
    private lateinit var eventService : EventService

    @Before
    fun `셋업`(){
        eventService = ApiBase().generateService(EventService::class.java,okHttpClient,callAdapter,converterFactory)
    }

    @Test
    @Throws(Exception::class)
    fun `ID_조회`(){
        eventService
            .getById(1L)
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValue{
                it.isOk && it.data != null
            }
            .assertComplete()
    }

    @Test
    @Throws(Exception::class)
    fun `위도_경도_조회`(){
        eventService
            .getByLocation("37.389019, 127.122628",1000)
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertValue{
                it.isOk && it.data != null && it.data?.isNotEmpty() == true
            }
            .assertComplete()
    }
}