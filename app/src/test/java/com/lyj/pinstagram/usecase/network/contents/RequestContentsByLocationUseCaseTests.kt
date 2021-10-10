package com.lyj.pinstagram.usecase.network.contents


import com.lyj.domain.usecase.network.contents.RequestContentsByLocationUseCase
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
import java.sql.Time
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [TestConfig.SDK_VERSION])
class RequestContentsByLocationUseCaseTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var requestContentsByLocationUseCase : RequestContentsByLocationUseCase

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun `실행테스트`(){
        requestContentsByLocationUseCase
            .execute(37.389019, 127.122628)
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertNoErrors()
            .assertValue {
                it.isOk && it.data != null && it.data!!.isNotEmpty()
            }
    }
}
