package com.lyj.data.source.remote.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.data.source.remote.http.ApiBase
import com.lyj.data.source.remote.http.event.EventService
import com.lyj.data.source.remote.http.geo.GeometryService
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class GeoServiceTests : BaseServiceTest() {
    private lateinit var geoService: GeometryService

    @Before
    fun `셋업`() {
        geoService = ApiBase().generateService(
            GeometryService::class.java,
            okHttpClient,
            callAdapter,
            converterFactory
        )
    }

    @Test
    @Throws(Exception::class)
    fun `리버스_지오코딩_테스트`() {
        geoService
            .getReverseGeocoding("37.389019,127.122628")
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertComplete()
            .assertValue {
                val result = it.data?.results
                if (!it.isOk || result == null){
                    false
                }else{
                    val validation = result[0]?.addressComponents?.any { item ->
                        val isContain = item?.longName?.contains("서현")
                        isContain != null && isContain
                    }
                    it.isOk && result != null && result.isNotEmpty() && validation != null && validation
                }
            }
    }
}