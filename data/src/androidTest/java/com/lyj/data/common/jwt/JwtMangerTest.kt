package com.lyj.data.common.jwt

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.core.extension.lang.testTag
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.crypto.Cipher.SECRET_KEY

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*
import kotlin.collections.HashMap


@RunWith(AndroidJUnit4::class)
class JwtMangerTest {
    private val EXPIRED_TIME = 1000 * 60L * 60L * 24L * 30L
    private val SECRET_KEY = "pinstagramsecret".toByteArray()

    lateinit var jwtManager: JwtManager
    lateinit var token : String
    private val email = "test@test.com"
    private val name = "김테스트"
    private val id = 1
    @Before
    fun `셋업`(){
        jwtManager = JwtManager()
        token = createToken(email,name,id)
    }

    private fun createToken(email: String, name : String, id: Int) : String{
        val heaaders: MutableMap<String, Any> = HashMap()
        heaaders["type"] = "JWT"
        heaaders["alg"] = "HS256"

        val payloads: MutableMap<String, Any> = HashMap()
        payloads["email"] = email
        payloads["name"] = name
        payloads["id"] = id

        val ext = Date()
        ext.time = ext.time + EXPIRED_TIME

        return Jwts
            .builder()
            .setHeader(heaaders)
            .setClaims(payloads)
            .setExpiration(ext)
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()
    }

    @Test
    fun `토큰_파싱_테스트`(){
        val data = jwtManager.parseJwt(token)
        Log.d(testTag,data.toString())
        assert(data?.email == "test@test.com")
        assert(data?.name == "김테스트")
        assert(data?.id == 1)
    }
}