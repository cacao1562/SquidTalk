package com.acacia.randomchat.api

import com.acacia.randomchat.BuildConfig
import com.acacia.randomchat.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RanChatRetrofit {

    private val interceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) level = (HttpLoggingInterceptor.Level.BODY)
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(3, TimeUnit.MINUTES)
        .readTimeout(3, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BaseURL)
        .client(okHttpClient)
        .build()

    fun getService(): RanChatService = retrofit.create(RanChatService::class.java)
}