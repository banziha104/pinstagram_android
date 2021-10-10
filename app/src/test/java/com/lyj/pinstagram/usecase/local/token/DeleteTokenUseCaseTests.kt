package com.lyj.pinstagram.usecase.local.token

import com.lyj.data.source.local.entity.TokenEntity
import com.lyj.domain.usecase.local.token.DeleteTokenUseCase
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
class DeleteTokenUseCaseTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var deleteTokenUseCase : DeleteTokenUseCase

    @Inject
    lateinit var insertTokenUseCase: InsertTokenUseCase

    @Before
    fun init(){
        hiltRule.inject()
        insertTokenUseCase
            .execute(TokenEntity(1,"test"))
            .test()
            .await()
    }

    @Test
    fun `실행테스트`(){
        deleteTokenUseCase
            .execute()
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
    }
}
