package com.acacia.randomchat.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface RanChatService {

    @Multipart
    @POST("/uploadImg")
    fun uploadImg(
        @Part file: List<MultipartBody.Part?>?,
        @PartMap params: HashMap<String, RequestBody>
    ): Call<ResponseBody?>?
}