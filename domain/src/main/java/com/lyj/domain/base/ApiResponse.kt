package com.lyj.domain.base

import android.net.sip.SipErrorCode
import android.net.sip.SipErrorCode.SERVER_ERROR


class ApiResponse<T>(
    val code: String,
    val message: String,
    val httpCode: Int,
    val data: T? = null
) {
    val isOk: Boolean
        get() = httpCode == 200

    fun getCodeType(): ApiResponseCode? = ApiResponseCode.findByCode(code)

    override fun toString(): String {
        return "ApiResponse(code='$code', message='$message', httpCode=$httpCode, data=$data, isOk=$isOk)"
    }
}

enum class ApiResponseCode(val message: String) {
    OK("요청이 성공하였습니다."),
    BAD_PARAMETER("요청 파라미터가 잘못되었습니다."),
    NOT_FOUND("리소스를 찾지 못했습니다."),
    SIGNIN_DUPLICATION("중복된 아이디 입니다."),
    UNAUTHORIZED("리소스에 접근하기 위한 권한이 없습니다"),
    FORBIDDEN("보유한 권한으로 접근할수 없는 리소스 입니다."),
    PASSWORD_NOT_CORRECT("패스워드가 일치하지 않습니다"),
    SERVER_ERROR("서버 에러입니다."),
    NO_CONTENTS("송신할 컨텐츠가 없습니다"),
    USER_NOT_FOUNDED("유저 정보가 없습니다"),
    UNSUPPORTED_MEDIA_TYPE("Unsupported Media Type");

    companion object {
        fun findByCode(name: String): ApiResponseCode? =
            values().firstOrNull { it.name == name }
    }

    fun getHttpCode(): Int = when (this) {
        OK -> 200
        BAD_PARAMETER, SIGNIN_DUPLICATION -> 400
        NOT_FOUND, USER_NOT_FOUNDED -> 404
        UNAUTHORIZED -> 401
        FORBIDDEN -> 403
        PASSWORD_NOT_CORRECT, SERVER_ERROR -> 500
        NO_CONTENTS -> 204
        UNSUPPORTED_MEDIA_TYPE -> 415
    }
}