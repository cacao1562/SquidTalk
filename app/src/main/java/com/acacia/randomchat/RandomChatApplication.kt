package com.acacia.randomchat

import android.app.Application
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.lang.RuntimeException
import java.net.URISyntaxException

class RandomChatApplication: Application() {

    private val mSocket: Socket? =
        try {
            IO.socket(Constants.BaseURL)
        }catch (e: URISyntaxException) {
//            throw RuntimeException(e)
            Log.d("tag", "error = $e")
            showToast("socket 연결 실패.")
            null
        }


    companion object {
        lateinit var instance: RandomChatApplication
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getSocket(): Socket? {
        return mSocket
    }

}