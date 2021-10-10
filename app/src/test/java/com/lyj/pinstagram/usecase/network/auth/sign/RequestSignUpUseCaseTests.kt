package com.lyj.pinstagram.usecase.network.auth.sign


import com.lyj.data.source.remote.entity.auth.request.SignInRequest
import com.lyj.data.source.remote.entity.auth.request.SignUpRequest
import com.lyj.domain.usecase.network.auth.sign.RequestSignUpUseCase
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
class RequestSignUpUseCaseTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var requestSignUpUseCase : RequestSignUpUseCase

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun `실행테스트`(){
//        별도의 테스트서버가 없어 실행하지않음
//        requestSignUpUseCase
//            .execute(SignUpRequest("banziha104@gmail.com","dl1212","김테스트"))
//            .test()
//            .await()
    }
}
