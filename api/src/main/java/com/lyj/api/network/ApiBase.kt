package com.lyj.api.network

import retrofit2.Retrofit

class ApiBase {
    private val baseUrl = "http://34.107.151.140"
    fun <T> createRepository(api : Class<T>) =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client()

}