package com.lyj.data.source.remote.network

import com.lyj.data.source.remote.http.ApiBase
import com.lyj.data.source.remote.http.ServiceGenerator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class BaseServiceTest {
    val serviceGenerator : ServiceGenerator = ApiBase()

    val callAdapter: CallAdapter.Factory by lazy { RxJava3CallAdapterFactory.create() }// RxConvererter
    val converterFactory: Converter.Factory by lazy {
        GsonConverterFactory
            .create()
    }// GsonFactory


    // okhttp 팩토리
    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().let {
            val  logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC
            it.addInterceptor(logger)
                .connectTimeout(20, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build()
        }
    }
}