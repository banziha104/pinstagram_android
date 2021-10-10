package com.lyj.pinstagram.usecase.network.event

import com.lyj.domain.usecase.network.event.RequestEventByIdUseCase
import com.lyj.pinstagram.usecase.TestConfig
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [TestConfig.SDK_VERSION])
class RequestEventByIdUseCaseTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var requestEventByIdUseCase : RequestEventByIdUseCase

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun `실행테스트`(){
        runBlocking {
            requestEventByIdUseCase
                .execute(1L)
                .collect {
                    assert(it.isOk && it.data != null && it.data!!.eventId == 1L)
                }
        }
    }
}
