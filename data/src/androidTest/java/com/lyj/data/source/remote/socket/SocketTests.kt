package com.lyj.data.source.remote.socket

import androidx.test.ext.junit.runners.AndroidJUnit4
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
    private val socketManager = SocketManager(null)

    @Before
    fun `00_셋업`(){

    }

    @Test
    fun `01_연결`(){
        socketManager
            .connect()
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `02_연결_해제`(){
        socketManager
            .disconnect()
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertNoErrors()
            .assertComplete()
    }
}