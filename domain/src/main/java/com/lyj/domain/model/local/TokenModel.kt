package com.lyj.domain.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

interface TokenModel{
    val id: Long
    val token: String
}
