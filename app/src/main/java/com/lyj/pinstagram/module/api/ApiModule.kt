package com.lyj.pinstagram.module.api

import com.lyj.data.source.remote.http.ServiceGenerator
import com.lyj.data.source.remote.http.auth.AuthenticationService
import com.lyj.data.source.remote.http.contents.ContentsService
import com.lyj.data.source.remote.http.event.EventService
import com.lyj.data.source.remote.http.geo.GeometryService
import com.lyj.data.source.remote.http.talk.TalkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

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
    ): GeometryService = serviceGenerator.generateService(
        GeometryService::class.java,
        client,
        callAdapter,
        converter
    )

    @Provides
    @Singleton
    fun provideEventApi(
        serviceGenerator: ServiceGenerator,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory,
        client: OkHttpClient
    ): EventService = serviceGenerator.generateService(
        EventService::class.java,
        client,
        callAdapter,
        converter
    )
}