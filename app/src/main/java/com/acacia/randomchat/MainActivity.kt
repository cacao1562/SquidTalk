package com.acacia.randomchat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mSocket: Socket? = null

    private var isConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSocket = RandomChatApplication.instance.getSocket()
        mSocket!!.on("connection", onConnect)
        mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket!!.on("user joined", onUserJoined)
        mSocket!!.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()

        mSocket?.off("connection", onConnect)
        mSocket?.off(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket?.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket?.off("user joined", onUserJoined)
    }
    private val onConnect = Emitter.Listener {
        Log.d("yhw", "MainActivity onConnect Listener\n \nby lines 45")
        CoroutineScope(Dispatchers.Main).launch {
            mSocket?.emit("addUser", UUID.randomUUID().toString())
            Toast.makeText(applicationContext, R.string.connect, Toast.LENGTH_SHORT).show()
            isConnected = true
        }
    }

    private val onDisconnect = Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            isConnected = false
            Toast.makeText(applicationContext, R.string.disconnect, Toast.LENGTH_SHORT).show()
        }
    }

    private val onConnectError = Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(applicationContext, R.string.error_connect, Toast.LENGTH_SHORT).show()
        }
    }

    private val onUserJoined = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            val data = args[0] as JSONObject
            val username: String
            val numUsers: Int
            try {
                username = data.getString("username")
                numUsers = data.getInt("numUsers")
                Log.d("yhw", "MainActivity userName=$username\n numUsers=$numUsers\nby lines 62")
            } catch (e: JSONException) {
                return@launch
            }
        }
    }

}