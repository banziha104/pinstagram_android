package com.lyj.data.common.jwt

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.core.extension.lang.testTag
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JwtMangerTest {
    val token = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJuYW1lIjoi6rmA7YWM7Iqk7Yq4IiwiaWQiOjEsImV4cCI6MTYyODk0ODMzNywiZW1haWwiOiJ0ZXN0QHRlc3QuY29tIn0.XegJSLM32MoZgn0Tgy_AhI5MiuH156G-7-atNJxoK38"

    lateinit var jwtManager: JwtManager

    @Before
    fun `셋업`(){
        jwtManager = JwtManager()
    }
    @Test
    fun `토큰_파싱_테스트`(){
        val data = jwtManager.parseJwt(token)
        Log.d(testTag,data.toString())
        assert(data?.email == "test@test.com")
        assert(data?.name == "김테스트")
        assert(data?.id == 1)
    }
}