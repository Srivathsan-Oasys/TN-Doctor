package com.hmis_tn.doctor.data.networking.client

import android.content.Context
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit


interface HttpClientManager {

    var client: OkHttpClient

    var gsonConverterFactory: GsonConverterFactory

    var retrofit: Retrofit

    fun displayErrorMessage(context: Context, message: String)

    fun disconnectUser(context: Context)

    companion object {

        internal const val TAG: String = "HttpClientManager"

        const val HEADER_KEY_AUTHORIZATION = "Authorization"

        const val TELEMED_SHARED_PREFERENCES = "telemed_shared_preferences"

        fun newInstance(): HttpClientManager = HttpClientManagerImpl()
    }

}

inline fun <reified T> HttpClientManager.createApi(): T {
    return this.retrofit.create()
}

private class HttpClientManagerImpl() : HttpClientManager {
    override var client: OkHttpClient =
        OkHttpClient.Builder()

            /*.addInterceptor {
                it.proceed(
                    it.request().newBuilder().addHeader(
                        HEADER_KEY_AUTHORIZATION,
                        "Bearer "+if(!accessToken.isNullOrEmpty())accessToken else APP_ACCESS_TOKEN
                    ).build()
                ).also {
                    handleError(it, context)
                }
            }*/
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
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(client)
        .build()

    override fun disconnectUser(context: Context) {
    }

    override fun displayErrorMessage(context: Context, message: String) {
    }
}
