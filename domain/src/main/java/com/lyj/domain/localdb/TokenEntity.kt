package com.lyj.domain.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token")
class TokenEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var token: String,
)