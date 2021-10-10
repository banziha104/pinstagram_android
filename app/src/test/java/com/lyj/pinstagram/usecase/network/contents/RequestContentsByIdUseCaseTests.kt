package com.lyj.pinstagram.usecase.network.contents


import com.lyj.domain.usecase.network.contents.RequestContentsByIdUseCase
import com.lyj.pinstagram.usecase.TestConfig
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
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
class RequestContentsByIdUseCaseTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var requestContentsByIdUseCase: RequestContentsByIdUseCase

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `실행테스트`() {
        runBlocking {
            assert(
                requestContentsByIdUseCase
                    .execute(1)
                    .let {
                        it.isOk && it.data != null && it.data!!.contentsId == 1L
                    }
            )
        }
    }
}
