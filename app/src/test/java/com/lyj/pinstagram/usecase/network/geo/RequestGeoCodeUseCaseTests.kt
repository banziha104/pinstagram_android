package com.lyj.pinstagram.usecase.network.geo

import com.lyj.domain.usecase.network.geo.RequestGeoCodeUseCase
import com.lyj.pinstagram.usecase.TestConfig
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [TestConfig.SDK_VERSION])
class RequestGeoCodeUseCaseTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var requestGeoCodeUseCase : RequestGeoCodeUseCase

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun `실행테스트`(){
        requestGeoCodeUseCase
            .execute("경기도 성남시 분당구 서현로 170")
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertNoErrors()
            .assertValue {
                it.isOk && it.data != null
            }
    }
}
