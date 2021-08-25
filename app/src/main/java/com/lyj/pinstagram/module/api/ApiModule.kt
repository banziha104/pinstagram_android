package com.lyj.pinstagram.module.api

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.lyj.api.network.ApiBase
import com.lyj.api.network.NetworkConnectionInterceptor
import com.lyj.api.network.ServiceGenerator
import com.lyj.api.network.auth.AuthenticationService
import com.lyj.api.network.contents.ContentsService
import com.lyj.api.network.geo.GeometrySerivce
import com.lyj.api.network.talk.TalkService
import com.lyj.core.extension.android.resString
import com.lyj.pinstagram.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideCallAdapterFactory(): CallAdapter.Factory = RxJava3CallAdapterFactory.create()

    @Provides
    @Singleton
    fun providerConvertFactory(): Converter.Factory = GsonConverterFactory
        .create()

    @Provides
    @Singleton
    fun providerOkHttpClient(@ApplicationContext context : Context): OkHttpClient = OkHttpClient.Builder().let {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        it
            .addInterceptor(logger)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .connectTimeout(20, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideServiceGenerator(): ServiceGenerator = ApiBase()

    @Provides
    @Singleton
    fun provideAuthApi(
        serviceGenerator: ServiceGenerator,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory,
        client: OkHttpClient
    ): AuthenticationService = serviceGenerator.generateService(
        AuthenticationService::class.java,
        client,
        callAdapter,
        converter
    )

    @Provides
    @Singleton
    fun provideContentsApi(
        serviceGenerator: ServiceGenerator,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory,
        client: OkHttpClient
    ): ContentsService = serviceGenerator.generateService(
        ContentsService::class.java,
        client,
        callAdapter,
        converter
    )

    @Provides
    @Singleton
    fun provideTalkApi(
        serviceGenerator: ServiceGenerator,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory,
        client: OkHttpClient
    ): TalkService = serviceGenerator.generateService(
        TalkService::class.java,
        client,
        callAdapter,
        converter
    )


    @Provides
    @Singleton
    fun provideGeoApi(
        serviceGenerator: ServiceGenerator,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory,
        client: OkHttpClient
    ): GeometrySerivce = serviceGenerator.generateService(
        GeometrySerivce::class.java,
        client,
        callAdapter,
        converter
    )
}