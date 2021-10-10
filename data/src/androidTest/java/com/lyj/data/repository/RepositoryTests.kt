package com.lyj.data.repository

import com.lyj.data.source.remote.network.BaseServiceTest

open class RepositoryTests : BaseServiceTest() {
    inline fun <reified T> generateService() = serviceGenerator.generateService(T::class.java,okHttpClient,callAdapter,converterFactory)
}