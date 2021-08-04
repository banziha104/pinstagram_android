package com.lyj.api.database.dao

import androidx.room.*
import com.lyj.domain.localdb.TokenEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface TokenDao {
    @Query("SELECT * FROM token where token.id == 1")
    fun findToken() : Single<List<TokenEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun refreshToken(entity: TokenEntity) : Completable

    @Query("DELETE FROM token WHERE id == :id")
    fun delete(id : Long = 1) : Completable
}