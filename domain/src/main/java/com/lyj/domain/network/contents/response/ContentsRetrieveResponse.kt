package com.lyj.domain.network.contents.response

import com.lyj.domain.network.contents.ContentsTagType

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