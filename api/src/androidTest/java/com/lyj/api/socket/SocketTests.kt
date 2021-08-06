package com.lyj.api.socket

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.core.extension.testTag
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SocketTests {
    private val socketManager = SocketManager(null){  }
    @Before
    fun `00_셋업`(){

    }

    @Test
    fun `01_연결`(){
        socketManager.connect().subscribe({

            Log.d(testTag, "연결 성공")
        },{
            Log.d(testTag, it.printStackTrace().toString())})
        socketManager
            .connect()
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `02_연결_해제`(){
        socketManager.disconnect().subscribe({
            Log.d(testTag, "해제 성공")
        },{
            Log.d(testTag, it.printStackTrace().toString())})
        socketManager
            .disconnect()
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertNoErrors()
            .assertComplete()
    }

    @After
    fun `03_종료`(){

    }
}