package com.acacia.randomchat.api

import com.acacia.randomchat.BuildConfig
import com.acacia.randomchat.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RanChatRetrofit {

    //    val BaseUrl = "http://221.149.225.95:80/" //이전 주소
    val BaseUrl = "http://192.168.0.11:3333/"
    //var BaseUrl = "http://192.168.0.85:8080/"

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