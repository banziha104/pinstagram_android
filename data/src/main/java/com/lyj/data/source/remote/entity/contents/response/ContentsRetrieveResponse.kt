package com.lyj.data.source.remote.entity.contents.response

import com.lyj.domain.model.AuthModel
import com.lyj.domain.model.network.contents.AccountModel
import com.lyj.domain.model.network.contents.ContentsModel
import com.lyj.domain.model.network.contents.ContentsTagType

data class ContentsRetrieveResponse(
    override val contentsId: Long,
    override val title: String,
    override val description: String,
    override val fullAddress: String,
    override val picture: String,
    override  val tag: ContentsTagType,
    override val lat: Double,
    override val lng: Double,
    override val account: AccountResponse,
    val createAt: String,
    val updateAt: String,
) : ContentsModel

data class AccountResponse(
    override val accountId: Long,
    override val name: String,
    override val email: String,
) : AccountModel