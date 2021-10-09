package com.lyj.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lyj.domain.model.TokenModel

const val TOKEN_ID = 1L

@Entity(tableName = "token")
class TokenEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    override val  token: String,
) : TokenModel
