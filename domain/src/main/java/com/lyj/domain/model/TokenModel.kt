package com.lyj.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

interface TokenModel{
    val id: Long
    val token: String
}
