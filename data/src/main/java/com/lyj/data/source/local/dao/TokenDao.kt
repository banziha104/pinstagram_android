package com.lyj.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lyj.data.source.local.entity.TokenEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface TokenDao {
    @Query("SELECT * FROM token where token.id == 1")
    fun findToken() : Single<List<TokenEntity>>

    @Query("SELECT * FROM token where token.id == 1")
    fun observeToken() : Flowable<List<TokenEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: TokenEntity) : Completable

    @Query("DELETE FROM token WHERE id == :id")
    fun delete(id : Long = 1) : Completable
}

class TokenIsNotValidated() : RuntimeException("토큰이 정상적이지 않습니다")