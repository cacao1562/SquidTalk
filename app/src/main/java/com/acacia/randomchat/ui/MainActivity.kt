package com.acacia.randomchat.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.acacia.randomchat.*
import com.acacia.randomchat.databinding.ActivityMainBinding
import com.acacia.randomchat.model.RoomData
import com.squareup.moshi.Moshi
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mSocket: Socket? = null

    private var isConnected = false

    private var mUUID = ""

    private lateinit var binding: ActivityMainBinding

    fun getSocket() = mSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mUUID = UUID.randomUUID().toString()

        mSocket = RandomChatApplication.instance.getSocket()
        mSocket?.on(Socket.EVENT_CONNECT, onConnect)
        mSocket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket?.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket?.on("user joined", onUserJoined)
        mSocket?.on("user searched", onUserSearched)
        mSocket?.on("user not searched", onUserNotSearched)
        mSocket?.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
        mSocket?.off(Socket.EVENT_CONNECT, onConnect)
        mSocket?.off(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket?.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket?.off("user joined", onUserJoined)
        mSocket?.off("user searched", onUserSearched)
        mSocket?.off("user not searched", onUserNotSearched)
    }

    private val onConnect = Emitter.Listener {
        Log.d("yhw", "[MainActivity>onConnect] connect successed. [57 lines]")
        CoroutineScope(Dispatchers.Main).launch {

//            SetNickNameFragment().show(supportFragmentManager, "dialog")

            Toast.makeText(applicationContext, R.string.connect, Toast.LENGTH_SHORT).show()
            isConnected = true
        }
    }
    private var doubleBackToExitPressedOnce = false

    private fun pressedBack() {
        if (doubleBackToExitPressedOnce) {
            finish()
            return
        }
        doubleBackToExitPressedOnce = true
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(applicationContext, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            delay(2000).run {
                doubleBackToExitPressedOnce = false
            }
        }
    }
    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.nav_host_container)?.let {
            if (it.childFragmentManager.backStackEntryCount == 0) {
                pressedBack()
            } else {
                super.onBackPressed()
            }
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
        Log.d("yhw", "[MainActivity>joined] userJoined [106 lines]")
        CoroutineScope(Dispatchers.Main).launch {
            val action = InputNickNameFragmentDirections.actionInputNickNameFragmentToMainFragment()
            findNavController(R.id.nav_host_container).safeNavigate(action)
        }
    }

    private val onUserSearched = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(applicationContext, "searched", Toast.LENGTH_SHORT).show()
            val data = args[0] as JSONObject
            Log.d("yhw", "[MainActivity>on Searched] data=$data [133 lines]")
            try {
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter<RoomData>(RoomData::class.java)
                val roomData = adapter.fromJson(args[0].toString())
                Log.d("yhw", "[MainActivity>] me=${roomData?.userMe}, you=${roomData?.userYou} [144 lines]")
                roomData?.let {
                    val action = MainFragmentDirections.actionMainFragmentToChatRoomFragment(it)
                    findNavController(R.id.nav_host_container).safeNavigate(action)
                }
            }catch (e: IOException) {
                e.printStackTrace()
            }catch (e: Exception) {
                e.printStackTrace()
            }
            
            supportFragmentManager.fragments.forEach { child ->
                if (child is HomeFragment) {
                    child.visibleLoading(false)
                }
            }

        }
    }

    private val onUserNotSearched = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(applicationContext, "user not found.", Toast.LENGTH_SHORT).show()
            supportFragmentManager.fragments.forEach { child ->
                if (child is HomeFragment) {
                    child.visibleLoading(false)
                }
            }
        }
    }

}