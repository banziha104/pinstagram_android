package com.lyj.pinstagram.usecase.android.location

import androidx.test.core.app.ActivityScenario
import com.lyj.domain.usecase.android.location.GetLocationUseCase
import com.lyj.pinstagram.usecase.TestConfig
import com.lyj.pinstagram.view.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [TestConfig.SDK_VERSION])
class GetLocationUseCaseTests {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var getLocationUseCase : GetLocationUseCase

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun `실행테스트`(){
//        val activity = Robolectric.buildActivity(MainActivity::class.java).setup()
//        getLocationUseCase
//            .execute(activity.get())!!
//            .test()
//            .awaitDone(3,TimeUnit.SECONDS)
//            .assertNoErrors()
//            .assertComplete()
    }
}
