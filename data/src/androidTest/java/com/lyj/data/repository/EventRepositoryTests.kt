package com.lyj.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.data.repository.network.EventRepositoryImpl
import com.lyj.domain.repository.network.EventRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class EventRepositoryTests : RepositoryTests() {

    private val eventRepository: EventRepository = EventRepositoryImpl(generateService())

    @Test
    @Throws(Exception::class)
    fun `ID_조회`() {
        runBlocking {
            assert(
                eventRepository
                    .getById(1L)
                    .let {
                        it.isOk && it.data != null && it.data!!.eventId == 1L
                    }
            )
        }
    }

    @Test
    @Throws(Exception::class)
    fun `위도_경도_조회`() {
        runBlocking {
            val response = eventRepository
                .getByLocation("37.389019, 127.122628", 1000)
            assert(response.let {
                it.isOk && it.data != null && it.data?.isNotEmpty() == true
            })
        }
    }
}
