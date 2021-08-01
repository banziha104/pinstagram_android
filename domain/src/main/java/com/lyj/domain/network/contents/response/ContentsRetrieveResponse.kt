package com.lyj.domain.network.contents.response

class ContentsRetrieveResponse(
    val contentsId: Long,
    val title: String,
    val description: String,
    val fullAddress: String,
    val picture: String,
    val tag: String,
    val lat: Double,
    val lng: Double,
    val account: AccountResponse,
    val createAt: String,
    val updateAt: String,
)

class AccountResponse(
    val accountId: Long,
    val name: String,
    val email: String,
)