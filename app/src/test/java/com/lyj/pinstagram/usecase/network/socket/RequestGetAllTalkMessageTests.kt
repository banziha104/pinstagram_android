package com.lyj.pinstagram.usecase.network.socket


import com.lyj.domain.usecase.network.socket.RequestGetAllTalkMessage
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
class RequestGetAllTalkMessageTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var requestGetAllTalkMessage : RequestGetAllTalkMessage

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun `실행테스트`(){
        requestGetAllTalkMessage
            .execute()
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertValue {
                it.isOk && it.data != null && it.data!!.isNotEmpty()
            }
    }
}
