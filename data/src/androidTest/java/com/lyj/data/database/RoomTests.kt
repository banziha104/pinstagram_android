package com.lyj.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.data.source.local.dao.TokenDao
import com.lyj.data.database.temp.localdb.TokenEntity
import com.lyj.data.source.local.LocalDatabase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RoomTests {
    private lateinit var tokenDao: TokenDao
    private lateinit var database: LocalDatabase
    private val compositeDisposable = CompositeDisposable()

    @Before
    fun `00_테스트_셋업`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, LocalDatabase::class.java
        ).build()
        tokenDao = database.tokenDao()
    }

    @Test
    @Throws(Exception::class)
    fun `01_토큰_생성`(){
        val token = "testToken"
        val request = TokenEntity(1L,token)
        compositeDisposable += tokenDao
            .refreshToken(request)
            .subscribe {  }

        tokenDao
            .findToken()
            .test()
            .assertComplete()
            .assertValue{
                it.size == 1 && it[0].id == 1L && it[0].token == "testToken"
            }
    }

    @Test
    @Throws(Exception::class)
    fun `02_토큰_삭제`(){
        compositeDisposable += tokenDao
            .delete()
            .subscribe {  }

        tokenDao
            .findToken()
            .test()
            .assertComplete()
            .assertValue{
                it.isEmpty()
            }
    }

    @After
    fun `99_테스트_완료처리`() {
        database.close()
        compositeDisposable.clear()
    }
}