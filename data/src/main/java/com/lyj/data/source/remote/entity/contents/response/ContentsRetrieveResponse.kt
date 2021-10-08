package com.lyj.data.source.remote.entity.contents.response

import com.lyj.domain.model.ContentsTagType

data class ContentsRetrieveResponse(
    val contentsId: Long,
    val title: String,
    val description: String,
    val fullAddress: String,
    val picture: String,
    val tag: ContentsTagType,
    val lat: Double,
    val lng: Double,
    val account: AccountResponse,
    val createAt: String,
    val updateAt: String,
)

data class AccountResponse(
    val accountId: Long,
    val name: String,
    val email: String,
)