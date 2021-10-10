package com.lyj.pinstagram.usecase.local.token

import com.lyj.data.source.local.entity.TokenEntity
import com.lyj.domain.usecase.local.token.InsertTokenUseCase
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
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [TestConfig.SDK_VERSION])
class InsertTokenUseCaseTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var insertTokenUseCase : InsertTokenUseCase

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun `실행테스트`(){
        insertTokenUseCase
            .execute(TokenEntity(1L,"id"))
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
    }
}
