package com.lyj.domain.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

const val TOKEN_ID = 1L

@Entity(tableName = "token")
class TokenEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var token: String,
)
