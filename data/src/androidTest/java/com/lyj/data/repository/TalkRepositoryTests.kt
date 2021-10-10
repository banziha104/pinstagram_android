package com.lyj.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.data.repository.network.TalkRepositoryImpl
import com.lyj.data.source.remote.socket.SocketManager
import com.lyj.domain.repository.network.TalkRepository
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class TalkRepositoryTests : RepositoryTests(){

    private val talkRepository: TalkRepository = TalkRepositoryImpl(generateService(),SocketManager.Factory)

    @Test
    fun `전체메세지_가져오기`(){
        talkRepository
            .getAllMessage()
            .test()
            .await()
            .assertValue {
                it.isOk && it.data != null && it.data!!.isNotEmpty()
            }
    }
}
