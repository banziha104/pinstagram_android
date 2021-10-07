package com.lyj.data.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter

import android.net.ConnectivityManager
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit


class ApiBase : ServiceGenerator {
    companion object {
        const val BASE_URL = "https://www.coguri.shop"
        const val ADDRESS_URL =  "$BASE_URL/geometry/address/index.html"
    }

    override fun <T> generateService(
        service: Class<T>,
        client: OkHttpClient,
        callAdapter: CallAdapter.Factory,
        converter: Converter.Factory
    ): T = Retrofit.Builder()
        .baseUrl(BASE_URL)
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

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected) {
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(context,"네트워크가 가용하지 않습니다. 네트워크 상태를 확인해주세요",Toast.LENGTH_LONG).show()
            }
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    private val isConnected: Boolean
        get() {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }
}
