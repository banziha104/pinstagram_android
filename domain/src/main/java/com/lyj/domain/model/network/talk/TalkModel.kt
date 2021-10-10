package com.lyj.domain.model.network.talk

import com.lyj.domain.model.network.auth.JwtModel

data class TalkModel(
    val name: String,
    val text: String,
    val userId: Long,
    val id: Long? = null
) {
    companion object {
        fun withAuth(jwtAuthData: JwtModel, text: String): TalkModel? =
            if (jwtAuthData.isValidated) TalkModel(
                jwtAuthData.name!!,
                text,
                jwtAuthData.id!!.toLong()
            ) else {
                null
            }
    }
}