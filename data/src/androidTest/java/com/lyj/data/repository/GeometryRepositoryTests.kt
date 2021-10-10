package com.lyj.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.data.repository.network.GeometryRepositoryImpl
import com.lyj.domain.repository.network.GeometryRepository
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class GeometryRepositoryTests : RepositoryTests(){

    private val geometryRepository: GeometryRepository = GeometryRepositoryImpl(generateService())

    @Test
    fun `지오코딩_테스트`(){
        geometryRepository
            .getGeocoding("경기도 성남시 서현로 170")
            .test()
            .await()
            .assertValue {
                it.isOk
            }
    }


    @Test
    fun `리버스지오코딩_테스트`(){
        geometryRepository
            .getReverseGeocoding("37.3889890630627,127.122531112568")
            .test()
            .await()
            .assertValue {
                it.isOk
            }
    }
}
