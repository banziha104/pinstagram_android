package com.lyj.pinstagram.usecase.network.geo

import com.lyj.domain.usecase.network.geo.RequestReversedGeoCodeUseCase
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
class RequestReversedGeoCodeUseCaseTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var requestReversedGeoCodeUseCase : RequestReversedGeoCodeUseCase

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun `실행테스트`(){
        requestReversedGeoCodeUseCase
            .execute(37.389019,127.122628)
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertValue {
                it != null && it.isNotBlank()
            }
    }
}
