package com.acacia.squidtalk

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SquidTalkApplication: Application() {

    private val mSocket: Socket? =
        try {
            IO.Options().reconnection
            IO.socket(Constants.BaseURL)
        }catch (e: URISyntaxException) {
            Toast.makeText(this, "socket 연결 실패.", Toast.LENGTH_LONG)
            null
        }


    companion object {
        lateinit var instance: SquidTalkApplication
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }

    fun getSocket(): Socket? {
        return mSocket
    }

}