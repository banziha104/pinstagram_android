package com.lyj.api.database

import androidx.room.Dao
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