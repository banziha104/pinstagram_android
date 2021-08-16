package com.lyj.api.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lyj.api.database.dao.TokenDao
import com.lyj.domain.localdb.TokenEntity

@Database(
    entities = [TokenEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase(){
    abstract fun tokenDao() : TokenDao
}

class DatabaseFailException : RuntimeException("데이터 베이스 작업이 실패하였습니다.")