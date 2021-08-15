package com.lyj.api.network

import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

class ApiBase : ServiceGenerator {
    private val baseUrl = "https://www.coguri.shop"

    override fun <T> generateService(
        service: Class<T>,
        client: OkHttpClient,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory
    ): T = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(callAdapter)
        .addConverterFactory(converter)
        .build()
        .create(service)
}

interface ServiceGenerator {
    fun <T> generateService(
        service: Class<T>,
        client: OkHttpClient,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory
    ) : T
}