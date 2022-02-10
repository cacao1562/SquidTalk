package com.acacia.randomchat

import android.app.Application
import android.util.Log
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class RandomChatApplication: Application() {

    private val mSocket: Socket? =
        try {
            IO.Options().reconnection
            IO.socket(Constants.BaseURL)
        }catch (e: URISyntaxException) {
            Log.d("tag", "error = $e")
            Toast.makeText(this, "socket 연결 실패.", Toast.LENGTH_LONG)
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