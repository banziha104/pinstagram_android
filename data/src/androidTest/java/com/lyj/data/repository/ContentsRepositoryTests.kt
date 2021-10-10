package com.lyj.data.repository


import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.data.mapper.ContentsMapper
import com.lyj.data.repository.network.ContentsRepositoryImpl
import com.lyj.domain.repository.network.ContentsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class ContentsRepositoryTests : RepositoryTests() {

    private val contentsRepository: ContentsRepository = ContentsRepositoryImpl(ContentsMapper(), generateService())

    @Test
    fun `아이디_조회`() {
        runBlocking {
            assert(contentsRepository
                .getById(1).let {
                    it.isOk && it.data != null && it.data!!.contentsId == 1L
                }
            )
        }
    }


    @Test
    fun `위도_경도_조회`() {
        contentsRepository
            .getByLocation("37.389019, 127.122628",1000)
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertValue {
                it.isOk && it.data != null && it.data!!.isNotEmpty()
            }
    }
}
