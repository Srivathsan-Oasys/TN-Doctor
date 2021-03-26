package com.hmis_tn.doctor.data.networking.client

import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit


interface HttpOpenTokClientManager {

    var client: OkHttpClient

    var gsonConverterFactory: GsonConverterFactory

    var retrofit: Retrofit

    companion object {
        fun newInstance(): HttpOpenTokClientManager =
            HttpOpenTokClientManagerImpl()
    }
}

inline fun <reified T> HttpOpenTokClientManager.createApi(): T {
    return this.retrofit.create()
}

private class HttpOpenTokClientManagerImpl() :
    HttpOpenTokClientManager {
    override var client: OkHttpClient = OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                addInterceptor(httpLoggingInterceptor.apply {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }
        .callTimeout(60, TimeUnit.SECONDS)
        .build()

    override var gsonConverterFactory: GsonConverterFactory =
        GsonConverterFactory.create(GsonBuilder().create())

    override var retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(BuildConfig.BASE_URL_OPENTOK)
        .addConverterFactory(gsonConverterFactory)
        .client(client)
        .build()
}
